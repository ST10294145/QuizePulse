package com.saihilg.quizepulse

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

    companion object {
        private const val TAG = "V2QuizActivity"
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

        // Disable button until questions load
        btnSubmit.isEnabled = false
        tvQuestion.text = "Loading questions..."

        // Get quiz type & difficulty from intent (force lowercase for Firestore)
        val quizType = intent.getStringExtra("quiz_type")?.lowercase() ?: "football"
        val difficulty = intent.getStringExtra("difficulty")?.lowercase() ?: "easy"

        Log.d(TAG, "Fetching questions for: $quizType / $difficulty")

        fetchQuestions(quizType, difficulty)

        btnSubmit.setOnClickListener { checkAnswer() }
    }

    private fun fetchQuestions(quizType: String, difficulty: String) {
        // Try WITHOUT orderBy first (in case index doesn't exist)
        db.collection("quizzes")
            .document(quizType)
            .collection(difficulty)
            .get(com.google.firebase.firestore.Source.SERVER) // Force server fetch, no cache
            .addOnSuccessListener { result ->
                Log.d(TAG, "Firestore success! Found ${result.size()} documents")

                val docIds = result.documents.map { "${it.id} (qNum: ${it.getLong("questionNumber")})" }
                Log.d(TAG, "Document IDs and question numbers: $docIds")

                if (result.isEmpty) {
                    Toast.makeText(this, "No questions found for $quizType - $difficulty", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "No questions in Firestore")
                    finish()
                    return@addOnSuccessListener
                }

                questions.clear()
                for (doc in result) {
                    try {
                        // LOG RAW DOCUMENT DATA
                        Log.d(TAG, "")
                        Log.d(TAG, "=== DOCUMENT ID: ${doc.id} ===")
                        Log.d(TAG, "All fields: ${doc.data}")

                        // Check if document has data
                        if (doc.data == null || doc.data!!.isEmpty()) {
                            Log.w(TAG, "⚠️ Document ${doc.id} is empty, skipping")
                            continue
                        }

                        // Manual parsing for reliability
                        val questionNumber = (doc.getLong("questionNumber") ?: 0).toInt()
                        val text = doc.getString("text") ?: ""

                        // Check for different field name variations
                        val optionsRaw = doc.get("options")
                            ?: doc.get("option")
                            ?: doc.get("Options")

                        Log.d(TAG, "Raw options field type: ${optionsRaw?.javaClass?.simpleName}")
                        Log.d(TAG, "Raw options field value: $optionsRaw")

                        val options = when (optionsRaw) {
                            is List<*> -> {
                                val parsed = optionsRaw.mapNotNull { it as? String }.toMutableList()
                                Log.d(TAG, "Parsed as List with ${parsed.size} items: $parsed")
                                parsed
                            }
                            else -> {
                                Log.e(TAG, "❌ Options is not a List! Type: ${optionsRaw?.javaClass}")
                                mutableListOf()
                            }
                        }

                        val answerIndex = (doc.getLong("answerIndex") ?: 0).toInt()

                        Log.d(TAG, "✓ Parsed Question #$questionNumber: $text")
                        Log.d(TAG, "  Options count: ${options.size}")
                        Log.d(TAG, "  Answer index: $answerIndex")

                        if (text.isEmpty()) {
                            Log.w(TAG, "⚠️ Skipping - empty question text")
                            continue
                        }

                        if (options.size < 3) {
                            Log.e(TAG, "⚠️ Skipping question #$questionNumber - only ${options.size} options (need 3+)")
                            continue
                        }

                        val q = Question(questionNumber, text, options, answerIndex)
                        questions.add(q)
                        Log.d(TAG, "✓ Added question #$questionNumber to list")

                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Error parsing document: ${doc.id}", e)
                        e.printStackTrace()
                    }
                }
                Log.d(TAG, "")

                if (questions.isEmpty()) {
                    Toast.makeText(this, "Failed to parse questions", Toast.LENGTH_LONG).show()
                    finish()
                    return@addOnSuccessListener
                }

                // Sort by questionNumber
                questions.sortBy { it.questionNumber }
                Log.d(TAG, "Sorted ${questions.size} questions")

                currentIndex = 0
                score = 0
                btnSubmit.isEnabled = true
                showQuestion()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Firestore fetch failed", e)
                Toast.makeText(this, "Failed to fetch questions: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
    }

    private fun showQuestion() {
        // Safety check
        if (questions.isEmpty()) {
            Log.e(TAG, "showQuestion called but questions list is empty!")
            Toast.makeText(this, "Error: No questions loaded", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (currentIndex >= questions.size) {
            showQuizFinished()
            return
        }

        val q = questions[currentIndex]

        Log.d(TAG, "Showing question ${currentIndex + 1}: ${q.text}")
        Log.d(TAG, "Options: ${q.options}")

        // Update question text with counter
        tvQuestion.text = "Question ${currentIndex + 1}/${questions.size}\n\n${q.text}"

        // CRITICAL: Clear selection FIRST
        rgOptions.clearCheck()

        // Validate options exist
        if (q.options.size < 3) {
            Log.e(TAG, "Question ${q.questionNumber} has only ${q.options.size} options!")
            Toast.makeText(this, "Error: Question has invalid options", Toast.LENGTH_SHORT).show()
            return
        }

        // Update RadioButton text and reset state
        rbOptionA.apply {
            text = q.options[0]
            isEnabled = true
            isChecked = false
            visibility = android.view.View.VISIBLE
        }

        rbOptionB.apply {
            text = q.options[1]
            isEnabled = true
            isChecked = false
            visibility = android.view.View.VISIBLE
        }

        rbOptionC.apply {
            text = q.options[2]
            isEnabled = true
            isChecked = false
            visibility = android.view.View.VISIBLE
        }

        // Update button text
        btnSubmit.text = if (currentIndex == questions.size - 1) {
            "Finish Quiz"
        } else {
            "Submit Answer"
        }
    }

    private fun checkAnswer() {
        // Safety checks
        if (questions.isEmpty()) {
            Log.e(TAG, "checkAnswer called but questions list is empty!")
            Toast.makeText(this, "Error: No questions loaded", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (currentIndex >= questions.size) {
            Log.e(TAG, "checkAnswer called but currentIndex ($currentIndex) >= questions.size (${questions.size})")
            showQuizFinished()
            return
        }

        val selectedIndex = when (rgOptions.checkedRadioButtonId) {
            R.id.rb_option_a_v2 -> 0
            R.id.rb_option_b_v2 -> 1
            R.id.rb_option_c_v2 -> 2
            else -> -1
        }

        if (selectedIndex == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val currentQuestion = questions[currentIndex]
        val correctIndex = currentQuestion.answerIndex

        Log.d(TAG, "Selected: $selectedIndex, Correct: $correctIndex")

        if (selectedIndex == correctIndex) {
            score++
            Toast.makeText(this, "Correct! ✓", Toast.LENGTH_SHORT).show()
        } else {
            val correctAnswer = currentQuestion.options.getOrNull(correctIndex) ?: "Unknown"
            Toast.makeText(
                this,
                "Wrong! Correct answer: $correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }

        currentIndex++
        Log.d(TAG, "Moving to question ${currentIndex + 1}")

        if (currentIndex >= questions.size) {
            showQuizFinished()
        } else {
            showQuestion()
        }
    }

    private fun showQuizFinished() {
        Log.d(TAG, "Quiz finished! Score: $score/${questions.size}")

        val percentage = if (questions.isNotEmpty()) {
            (score * 100 / questions.size)
        } else {
            0
        }

        tvQuestion.text = "🎉 Quiz Finished! 🎉\n\n" +
                "Your Score: $score/${questions.size}\n" +
                "Percentage: $percentage%"

        // Hide all radio buttons
        rbOptionA.visibility = android.view.View.GONE
        rbOptionB.visibility = android.view.View.GONE
        rbOptionC.visibility = android.view.View.GONE

        btnSubmit.text = "Back to Quiz Selection"
        btnSubmit.setOnClickListener {
            finish()
        }
    }
}