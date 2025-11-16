package com.saihilg.quizepulse

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.saihilg.quizepulse.model.Question

class V2QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var rbOptionA: RadioButton
    private lateinit var rbOptionB: RadioButton
    private lateinit var rbOptionC: RadioButton
    private lateinit var btnSubmit: Button

    private val db = FirebaseFirestore.getInstance()
    private var questions = mutableListOf<Question>()
    private var currentIndex = 0
    private var score = 0
    private var currentDifficulty = "easy"

    companion object {
        private const val TAG = "V2QuizActivity"
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("lang", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v2quiz)

        // Bind views
        tvQuestion = findViewById(R.id.tv_question_text_v2)
        rgOptions = findViewById(R.id.rg_options_v2)
        rbOptionA = findViewById(R.id.rb_option_a_v2)
        rbOptionB = findViewById(R.id.rb_option_b_v2)
        rbOptionC = findViewById(R.id.rb_option_c_v2)
        btnSubmit = findViewById(R.id.btn_submit_v2)

        btnSubmit.isEnabled = false
        tvQuestion.text = getString(R.string.loading_questions)

        // Get quiz type & difficulty
        val quizType = intent.getStringExtra("quiz_type")?.lowercase() ?: "football"
        val difficulty = intent.getStringExtra("difficulty")?.lowercase() ?: "easy"
        currentDifficulty = difficulty

        Log.d(TAG, "Fetching questions for: $quizType / $difficulty")

        fetchQuestions(quizType, difficulty)

        btnSubmit.setOnClickListener { checkAnswer() }
    }

    private fun fetchQuestions(quizType: String, difficulty: String) {
        db.collection("quizzes")
            .document(quizType)
            .collection(difficulty)
            .get()
            .addOnSuccessListener { result ->

                if (result.isEmpty) {
                    Toast.makeText(
                        this,
                        getString(R.string.no_questions_found, quizType, difficulty),
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                    return@addOnSuccessListener
                }

                questions.clear()
                for (doc in result) {
                    try {
                        val questionNumber = (doc.getLong("questionNumber") ?: 0).toInt()
                        val text = doc.getString("text") ?: ""
                        val options = (doc.get("options") as? List<*>)
                            ?.mapNotNull { it as? String }
                            ?.toMutableList() ?: mutableListOf()
                        val answerIndex = (doc.getLong("answerIndex") ?: 0).toInt()

                        if (text.isNotEmpty() && options.size >= 3) {
                            questions.add(Question(questionNumber, text, options, answerIndex))
                        }
                    } catch (_: Exception) { }
                }

                if (questions.isEmpty()) {
                    Toast.makeText(this, getString(R.string.failed_to_parse_questions), Toast.LENGTH_LONG).show()
                    finish()
                    return@addOnSuccessListener
                }

                questions.sortBy { it.questionNumber }
                currentIndex = 0
                score = 0
                btnSubmit.isEnabled = true

                showQuestion()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.failed_to_fetch_questions, e.message),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
    }

    private fun showQuestion() {
        if (currentIndex >= questions.size) {
            showQuizFinished()
            return
        }

        val q = questions[currentIndex]
        tvQuestion.text = getString(
            R.string.question_format,
            currentIndex + 1,
            questions.size,
            q.text
        )

        rgOptions.clearCheck()

        rbOptionA.text = q.options[0]
        rbOptionB.text = q.options[1]
        rbOptionC.text = q.options[2]

        btnSubmit.text = if (currentIndex == questions.size - 1)
            getString(R.string.finish_quiz)
        else
            getString(R.string.submit_answer)
    }

    private fun checkAnswer() {
        val selectedIndex = when (rgOptions.checkedRadioButtonId) {
            R.id.rb_option_a_v2 -> 0
            R.id.rb_option_b_v2 -> 1
            R.id.rb_option_c_v2 -> 2
            else -> -1
        }

        if (selectedIndex == -1) {
            Toast.makeText(this, getString(R.string.please_select_answer), Toast.LENGTH_SHORT).show()
            return
        }

        val currentQuestion = questions[currentIndex]
        val correctIndex = currentQuestion.answerIndex

        if (selectedIndex == correctIndex) {
            val pointsEarned = when (currentDifficulty) {
                "easy" -> 1
                "hard" -> 3
                else -> 1
            }
            score += pointsEarned
            updateUserPoints(pointsEarned)
            Toast.makeText(this, getString(R.string.correct_points, pointsEarned), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                getString(R.string.wrong_answer, currentQuestion.options[correctIndex]),
                Toast.LENGTH_SHORT
            ).show()
        }

        currentIndex++
        if (currentIndex >= questions.size) showQuizFinished() else showQuestion()
    }

    private fun updateUserPoints(points: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users").document(uid)
            .update("totalPoints", FieldValue.increment(points.toLong()))
    }

    private fun showQuizFinished() {
        tvQuestion.text = getString(R.string.quiz_finished_with_score, score, questions.size)

        rbOptionA.visibility = android.view.View.GONE
        rbOptionB.visibility = android.view.View.GONE
        rbOptionC.visibility = android.view.View.GONE

        btnSubmit.text = getString(R.string.back_to_selection)
        btnSubmit.setOnClickListener { finish() }
    }
}
