package com.saihilg.quizepulse

import org.junit.Assert.*
import org.junit.Test

class RegisterValidatorTest {

    @Test
    fun emptyName_returnsError() {
        val result = RegisterValidator.validate("", "test@email.com", "123456")
        assertFalse(result.isValid)
        assertEquals("Enter your name", result.error)
    }

    @Test
    fun emptyEmail_returnsError() {
        val result = RegisterValidator.validate("John", "", "123456")
        assertFalse(result.isValid)
        assertEquals("Enter email", result.error)
    }

    @Test
    fun passwordTooShort_returnsError() {
        val result = RegisterValidator.validate("John", "test@email.com", "123")
        assertFalse(result.isValid)
        assertEquals("Password must be at least 6 characters", result.error)
    }

    @Test
    fun validInput_returnsValid() {
        val result = RegisterValidator.validate("John", "test@email.com", "123456")
        assertTrue(result.isValid)
        assertNull(result.error)
    }
}
