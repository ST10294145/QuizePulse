package com.saihilg.quizepulse

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var rbOptionA: RadioButton
    private lateinit var rbOptionB: RadioButton
    private lateinit var rbOptionC: RadioButton
    private lateinit var rbOptionD: RadioButton
    private lateinit var btnSubmit: Button

    // Question sets
    private val footballQuestions = listOf(
        QuizQuestion("Who won the 2018 World Cup?", listOf("Brazil", "France", "Germany", "Argentina"), 1),
        QuizQuestion("Which player has the most goals in World Cup history?", listOf("Müller", "Pelé", "Klose", "Messi"), 2)
    )

    private val musicQuestions = listOf(
        QuizQuestion("Who sang 'Thriller'?", listOf("Prince", "Michael Jackson", "Madonna", "Elton John"), 1),
        QuizQuestion("Which band released 'Bohemian Rhapsody'?", listOf("Queen", "The Beatles", "Pink Floyd", "ABBA"), 0)
    )

    private val popCultureQuestions = listOf(
        QuizQuestion("Which movie won Best Picture in 2020?", listOf("1917", "Parasite", "Joker", "Ford v Ferrari"), 1),
        QuizQuestion("Who is known as the 'King of TikTok'?", listOf("Charli D'Amelio", "Addison Rae", "Bella Poarch", "Dixie D'Amelio"), 0)
    )

    // Dynamic question list
    private lateinit var questions: List<QuizQuestion>
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Bind views
        tvQuestion = findViewById(R.id.tv_question_text)
        rgOptions = findViewById(R.id.rg_options)
        rbOptionA = findViewById(R.id.rb_option_a)
        rbOptionB = findViewById(R.id.rb_option_b)
        rbOptionC = findViewById(R.id.rb_option_c)
        rbOptionD = findViewById(R.id.rb_option_d)
        btnSubmit = findViewById(R.id.btn_submit)

        // Get quiz type from Intent
        val quizType = intent.getStringExtra("quiz_type")
        questions = when (quizType) {
            "football" -> footballQuestions
            "music" -> musicQuestions
            "popculture" -> popCultureQuestions
            else -> emptyList()
        }

        if (questions.isEmpty()) {
            Toast.makeText(this, "No questions available for this quiz.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Load first question
        loadQuestion()

        btnSubmit.setOnClickListener { submitAnswer() }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex >= questions.size) {
            // Quiz finished
            Toast.makeText(this, "Quiz finished! Score: $score/${questions.size}", Toast.LENGTH_LONG).show()
            finish() // Or navigate to a results activity
            return
        }

        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.question
        rbOptionA.text = question.options[0]
        rbOptionB.text = question.options[1]
        rbOptionC.text = question.options[2]
        rbOptionD.text = question.options[3]

        rgOptions.clearCheck()
    }

    private fun submitAnswer() {
        val selectedOptionId = rgOptions.checkedRadioButtonId
        if (selectedOptionId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedIndex = when (selectedOptionId) {
            R.id.rb_option_a -> 0
            R.id.rb_option_b -> 1
            R.id.rb_option_c -> 2
            R.id.rb_option_d -> 3
            else -> -1
        }

        if (selectedIndex == questions[currentQuestionIndex].correctAnswerIndex) {
            score++
        }

        currentQuestionIndex++
        loadQuestion()
    }
}
