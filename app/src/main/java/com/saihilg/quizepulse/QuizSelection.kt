package com.saihilg.quizepulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class QuizSelection : AppCompatActivity() {

    private lateinit var btnFootballQuiz: Button
    private lateinit var btnMusicQuiz: Button
    private lateinit var btnPopCultureQuiz: Button
    private lateinit var btnSettings: ImageButton
    private lateinit var bottomNav: BottomNavigationView

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("lang", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_selection)

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind buttons
        btnFootballQuiz = findViewById(R.id.btnFootballQuiz)
        btnMusicQuiz = findViewById(R.id.btnMusicQuiz)
        btnPopCultureQuiz = findViewById(R.id.btnPopCultureQuiz)
        btnSettings = findViewById(R.id.btnSettings)
        bottomNav = findViewById(R.id.bottomNavigationView)

        // Set click listeners for quizzes
        btnFootballQuiz.setOnClickListener { showDifficultyDialog("football") }
        btnMusicQuiz.setOnClickListener { showDifficultyDialog("music") }
        btnPopCultureQuiz.setOnClickListener { showDifficultyDialog("popculture") }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Make BottomNavigationView functional
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }

                //TODO: Add results activity

                R.id.nav_leaderboard -> {
                    startActivity(Intent(this, LeaderboardActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // Function to show difficulty selection dialog
    private fun showDifficultyDialog(quizType: String) {
        val options = arrayOf(
            getString(R.string.difficulty_easy),
            getString(R.string.difficulty_hard)
        )
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_difficulty))
            .setItems(options) { _, which ->
                val difficulty = if (which == 0) "easy" else "hard"
                startV2Quiz(quizType.lowercase(), difficulty)
            }
            .setCancelable(true)
            .show()
    }

    // Launch the Firestore-powered quiz activity
    private fun startV2Quiz(quizType: String, difficulty: String) {
        val intent = Intent(this, V2QuizActivity::class.java)
        intent.putExtra("quiz_type", quizType)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }
}
