package com.saihilg.quizepulse

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etDisplayName: EditText
    private lateinit var btnUpdateName: Button
    private lateinit var etNewPassword: EditText
    private lateinit var btnUpdatePassword: Button
    private lateinit var btnLogout: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Views
        tvUserName = findViewById(R.id.tvUserName)
        tvEmail = findViewById(R.id.tv_email)
        etDisplayName = findViewById(R.id.et_display_name)
        btnUpdateName = findViewById(R.id.btn_update_name)
        etNewPassword = findViewById(R.id.et_new_password)
        btnUpdatePassword = findViewById(R.id.btn_update_password)
        btnLogout = findViewById(R.id.btn_logout)

        // Firebase
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = mAuth.currentUser
        userId = currentUser?.uid ?: ""

        if (currentUser != null) {
            loadUserInfo()
        }

        btnUpdateName.setOnClickListener { updateDisplayName() }
        btnUpdatePassword.setOnClickListener { updatePassword() }
        btnLogout.setOnClickListener { logoutUser() }
    }

    private fun loadUserInfo() {
        // Get user info from Firestore
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name") ?: "User"
                    val email = document.getString("email") ?: ""
                    tvUserName.text = name
                    tvEmail.text = "Email: $email"
                    etDisplayName.setText(name)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load user info: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateDisplayName() {
        val newName = etDisplayName.text.toString().trim()
        if (newName.isEmpty()) {
            etDisplayName.error = "Enter a name"
            return
        }

        // Update Firestore
        db.collection("users").document(userId)
            .update("name", newName)
            .addOnSuccessListener {
                tvUserName.text = newName
                Toast.makeText(this, "Display name updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update name: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updatePassword() {
        val newPassword = etNewPassword.text.toString().trim()
        if (newPassword.length < 6) {
            etNewPassword.error = "Password must be at least 6 characters"
            return
        }

        currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                etNewPassword.text.clear()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update password: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun logoutUser() {
        mAuth.signOut()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        finish() // or navigate to LoginActivity
    }
}
