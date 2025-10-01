package com.saihilg.quizepulse

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase
        mAuth = FirebaseAuth.getInstance()

        // Views
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.login_button)

        //link to RegisterActivity
        tvRegister = TextView(this).apply {
            text = "Don't have an account? Sign up"
            setTextColor(resources.getColor(android.R.color.white))
        }

        // Login button click
        btnLogin.setOnClickListener { loginUser() }

        // Navigate to Register page
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate input
        when {
            TextUtils.isEmpty(email) -> {
                etEmail.error = "Enter email"
                return
            }
            TextUtils.isEmpty(password) -> {
                etPassword.error = "Enter password"
                return
            }
            password.length < 6 -> {
                etPassword.error = "Password must be at least 6 characters"
                return
            }
        }

        // Firebase login
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, QuizSelection::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
