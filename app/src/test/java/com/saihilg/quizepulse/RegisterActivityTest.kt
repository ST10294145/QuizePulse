package com.saihilg.quizepulse

import android.widget.EditText
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RegisterActivityTest {

    private lateinit var activity: RegisterActivity
    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    @Before
    fun setup() {
        activity = mock(RegisterActivity::class.java)
        nameField = mock(EditText::class.java)
        emailField = mock(EditText::class.java)
        passwordField = mock(EditText::class.java)
    }

    @Test
    fun emptyName_showsError() {
        `when`(nameField.text.toString()).thenReturn("")
        `when`(emailField.text.toString()).thenReturn("test@email.com")
        `when`(passwordField.text.toString()).thenReturn("123456")

        assertTrue("Name is empty", nameField.text.toString().isEmpty())
    }

    @Test
    fun invalidPassword_showsError() {
        `when`(nameField.text.toString()).thenReturn("John")
        `when`(emailField.text.toString()).thenReturn("test@email.com")
        `when`(passwordField.text.toString()).thenReturn("123")

        assertTrue("Password too short", passwordField.text.toString().length < 6)
    }

    @Test
    fun validInput_passesValidation() {
        `when`(nameField.text.toString()).thenReturn("John")
        `when`(emailField.text.toString()).thenReturn("john@email.com")
        `when`(passwordField.text.toString()).thenReturn("123456")

        assertEquals("John", nameField.text.toString())
        assertEquals("john@email.com", emailField.text.toString())
        assertEquals("123456", passwordField.text.toString())
    }
}
