package com.saihilg.quizepulse

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvSubtitle: TextView
    private lateinit var btnGoogleSignIn: SignInButton

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Firebase
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Views
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnSignup = findViewById(R.id.btn_signup)
        tvSubtitle = findViewById(R.id.tv_subtitle)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Register launcher for result
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(Exception::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: Exception) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Button listeners
        btnSignup.setOnClickListener { registerUser() }
        btnGoogleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

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

                        // Save user to Firestore with totalPoints = 0
                        saveUserToFirestore(user.uid, name, email)

                        Toast.makeText(this, "Account created for $name", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, QuizSelection::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user != null) {

                        // Save Google user to Firestore with totalPoints = 0
                        saveUserToFirestore(
                            user.uid,
                            user.displayName ?: "No Name",
                            user.email ?: ""
                        )

                        Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, QuizSelection::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Google Sign-In failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String) {
        val userMap = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "createdAt" to FieldValue.serverTimestamp(),
            "totalPoints" to 0
        )

        db.collection("users")
            .document(uid)
            .set(userMap)
            .addOnSuccessListener {
                // User saved successfully
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
