package com.saihilg.quizepulse

import org.junit.Assert.*
import org.junit.Test

class LoginValidatorTest {

    @Test
    fun emailEmpty_returnsError() {
        val result = LoginValidator.validate("", "123456")
        assertFalse(result.isValid)
        assertEquals("Enter email", result.error)
    }

    @Test
    fun passwordEmpty_returnsError() {
        val result = LoginValidator.validate("test@gmail.com", "")
        assertFalse(result.isValid)
        assertEquals("Enter password", result.error)
    }

    @Test
    fun passwordTooShort_returnsError() {
        val result = LoginValidator.validate("test@gmail.com", "123")
        assertFalse(result.isValid)
        assertEquals("Password must be at least 6 characters", result.error)
    }

    @Test
    fun validInput_returnsValid() {
        val result = LoginValidator.validate("test@gmail.com", "123456")
        assertTrue(result.isValid)
        assertNull(result.error)
    }
}
