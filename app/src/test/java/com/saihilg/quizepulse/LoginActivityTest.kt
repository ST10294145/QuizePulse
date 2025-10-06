package com.saihilg.quizepulse

import android.widget.EditText
import android.widget.Button
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock

class LoginActivityTest {

    private lateinit var activity: LoginActivity

    @Before
    fun setUp() {
        activity = mock()
        // Mock the EditTexts and Button to prevent null references
        val emailField = mock<EditText>()
        val passwordField = mock<EditText>()
        val loginButton = mock<Button>()

        // Use reflection to set private fields
        val emailFieldField = LoginActivity::class.java.getDeclaredField("etEmail")
        val passwordFieldField = LoginActivity::class.java.getDeclaredField("etPassword")
        val buttonField = LoginActivity::class.java.getDeclaredField("btnLogin")

        emailFieldField.isAccessible = true
        passwordFieldField.isAccessible = true
        buttonField.isAccessible = true

        emailFieldField.set(activity, emailField)
        passwordFieldField.set(activity, passwordField)
        buttonField.set(activity, loginButton)
    }

    @Test
    fun `email empty should show error`() {
        val emailField = mock<EditText>()
        `when`(emailField.text).thenReturn(android.text.Editable.Factory.getInstance().newEditable(""))

        val passwordField = mock<EditText>()
        `when`(passwordField.text).thenReturn(android.text.Editable.Factory.getInstance().newEditable("123456"))

        activity.apply {
            this::class.java.getDeclaredField("etEmail").apply {
                isAccessible = true
                set(this@apply, emailField)
            }
            this::class.java.getDeclaredField("etPassword").apply {
                isAccessible = true
                set(this@apply, passwordField)
            }
        }

        activity.runOnUiThread {
            activity.javaClass.getDeclaredMethod("loginUser").apply {
                isAccessible = true
                invoke(activity)
            }
        }

        verify(emailField).error = "Enter email"
    }
}
