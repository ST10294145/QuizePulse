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
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvSubtitle: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Firebase
        mAuth = FirebaseAuth.getInstance()

        // Views
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnSignup = findViewById(R.id.btn_signup)
        tvSubtitle = findViewById(R.id.tv_subtitle)

        // Sign Up Button
        btnSignup.setOnClickListener { registerUser() }

        // Navigate to Login page
        tvSubtitle.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        when {
            TextUtils.isEmpty(name) -> {
                etName.error = "Enter your name"
                return
            }
            TextUtils.isEmpty(email) -> {
                etEmail.error = "Enter email"
                return
            }
            TextUtils.isEmpty(password) || password.length < 6 -> {
                etPassword.error = "Password must be at least 6 characters"
                return
            }
        }

        // Firebase create user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = mAuth.currentUser
                    if (user != null) {
                        Toast.makeText(
                            this,
                            "Account created for $name",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Redirect to Login or Home
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
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
