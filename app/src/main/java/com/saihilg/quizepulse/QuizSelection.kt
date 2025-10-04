package com.saihilg.quizepulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizSelection : AppCompatActivity() {

    private lateinit var btnFootballQuiz: Button
    private lateinit var btnMusicQuiz: Button
    private lateinit var btnPopCultureQuiz: Button

    private lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_selection)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind buttons
        btnFootballQuiz = findViewById(R.id.btnFootballQuiz)
        btnMusicQuiz = findViewById(R.id.btnMusicQuiz)
        btnPopCultureQuiz = findViewById(R.id.btnPopCultureQuiz)

        // Set click listeners
        btnFootballQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quiz_type", "football")
            startActivity(intent)
        }

        btnMusicQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quiz_type", "music")
            startActivity(intent)
        }

        btnPopCultureQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quiz_type", "popculture")
            startActivity(intent)
        }

        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}
