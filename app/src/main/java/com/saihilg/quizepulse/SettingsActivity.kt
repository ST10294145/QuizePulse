package com.saihilg.quizepulse

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etDisplayName: EditText
    private lateinit var btnUpdateName: Button
    private lateinit var spinnerLanguage: Spinner
    private lateinit var btnLogout: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null
    private var userId: String = ""

    // Supported languages
    private val languages = mapOf(
        "English" to "en",
        "Zulu" to "zu",
        "Afrikaans" to "af"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Views
        tvUserName = findViewById(R.id.tvUserName)
        tvEmail = findViewById(R.id.tv_email)
        etDisplayName = findViewById(R.id.et_display_name)
        btnUpdateName = findViewById(R.id.btn_update_name)
        spinnerLanguage = findViewById(R.id.spinner_language)
        btnLogout = findViewById(R.id.btn_logout)

        // Firebase
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = mAuth.currentUser
        userId = currentUser?.uid ?: ""

        if (currentUser != null) {
            loadUserInfo()
        }

        setupLanguageSpinner()

        btnUpdateName.setOnClickListener { updateDisplayName() }
        btnLogout.setOnClickListener { logoutUser() }
    }

    private fun loadUserInfo() {
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

    private fun setupLanguageSpinner() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val selectedLang = prefs.getString("language_code", "en") ?: "en"

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languages.keys.toList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        // Set current selection
        val index = languages.values.toList().indexOf(selectedLang)
        spinnerLanguage.setSelection(if (index >= 0) index else 0)

        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val langCode = languages.values.toList()[position]
                prefs.edit().putString("language_code", langCode).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun logoutUser() {
        mAuth.signOut()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        finish()
    }
}
