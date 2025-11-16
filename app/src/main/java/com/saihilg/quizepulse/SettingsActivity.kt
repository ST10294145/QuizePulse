package com.saihilg.quizepulse

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var tvSettingsTitle: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etDisplayName: EditText
    private lateinit var btnUpdateName: Button
    private lateinit var etNewPassword: EditText
    private lateinit var btnUpdatePassword: Button
    private lateinit var btnLogout: Button

    // Language selection views
    private lateinit var radioGroupLanguage: RadioGroup
    private lateinit var rbEnglish: RadioButton
    private lateinit var rbAfrikaans: RadioButton
    private lateinit var rbZulu: RadioButton
    private lateinit var btnApplyLanguage: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null
    private var userId: String = ""

    // Apply language before activity starts
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("lang", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Bind views
        tvSettingsTitle = findViewById(R.id.tvSettingsTitle)
        tvUserName = findViewById(R.id.tvUserName)
        tvEmail = findViewById(R.id.tv_email)
        etDisplayName = findViewById(R.id.et_display_name)
        btnUpdateName = findViewById(R.id.btn_update_name)
        etNewPassword = findViewById(R.id.et_new_password)
        btnUpdatePassword = findViewById(R.id.btn_update_password)
        btnLogout = findViewById(R.id.btn_logout)

        radioGroupLanguage = findViewById(R.id.radioGroupLanguage)
        rbEnglish = findViewById(R.id.rbEnglish)
        rbAfrikaans = findViewById(R.id.rbAfrikaans)
        rbZulu = findViewById(R.id.rbZulu)
        btnApplyLanguage = findViewById(R.id.btnApplyLanguage)

        // Firebase
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = mAuth.currentUser
        userId = currentUser?.uid ?: ""

        // Load user info
        if (currentUser != null) loadUserInfo()

        // Set all texts dynamically from strings.xml
        tvSettingsTitle.text = getString(R.string.settings_title)
        etDisplayName.hint = getString(R.string.enter_new_display_name)
        etNewPassword.hint = getString(R.string.enter_new_password)
        btnUpdateName.text = getString(R.string.update_name)
        btnUpdatePassword.text = getString(R.string.update_password)
        btnLogout.text = getString(R.string.logout)
        btnApplyLanguage.text = getString(R.string.apply_language)

        // Set radio button texts from resources
        rbEnglish.text = getString(R.string.english)
        rbAfrikaans.text = getString(R.string.afrikaans)
        rbZulu.text = getString(R.string.zulu)

        // Button listeners
        btnUpdateName.setOnClickListener { updateDisplayName() }
        btnUpdatePassword.setOnClickListener { updatePassword() }
        btnLogout.setOnClickListener { logoutUser() }
        btnApplyLanguage.setOnClickListener { applyLanguage() }

        // Set current language radio button
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("lang", "en") ?: "en"
        when (lang) {
            "en" -> rbEnglish.isChecked = true
            "af" -> rbAfrikaans.isChecked = true
            "zu" -> rbZulu.isChecked = true
        }
    }

    private fun loadUserInfo() {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name") ?: "User"
                    val email = document.getString("email") ?: ""
                    tvUserName.text = name
                    tvEmail.text = getString(R.string.email_label, email)
                    etDisplayName.setText(name)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.failed_load_user_info, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun updateDisplayName() {
        val newName = etDisplayName.text.toString().trim()
        if (newName.isEmpty()) {
            etDisplayName.error = getString(R.string.enter_name_error)
            return
        }

        db.collection("users").document(userId)
            .update("name", newName)
            .addOnSuccessListener {
                tvUserName.text = newName
                Toast.makeText(this, getString(R.string.name_updated), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.failed_update_name, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun updatePassword() {
        val newPassword = etNewPassword.text.toString().trim()
        if (newPassword.length < 6) {
            etNewPassword.error = getString(R.string.password_length_error)
            return
        }

        currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                Toast.makeText(this, getString(R.string.password_updated), Toast.LENGTH_SHORT).show()
                etNewPassword.text.clear()
            }
            ?.addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.failed_update_password, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun logoutUser() {
        mAuth.signOut()
        Toast.makeText(this, getString(R.string.logged_out), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun applyLanguage() {
        val selectedLang = when (radioGroupLanguage.checkedRadioButtonId) {
            R.id.rbEnglish -> "en"
            R.id.rbAfrikaans -> "af"
            R.id.rbZulu -> "zu"
            else -> "en"
        }

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val currentLang = prefs.getString("lang", "en") ?: "en"

        // Only recreate if language actually changed
        if (selectedLang != currentLang) {
            prefs.edit().putString("lang", selectedLang).apply()

            // Show toast BEFORE recreating
            Toast.makeText(this, getString(R.string.language_changed), Toast.LENGTH_SHORT).show()

            // Recreate activity to apply the new language
            recreate()
        }
    }
}