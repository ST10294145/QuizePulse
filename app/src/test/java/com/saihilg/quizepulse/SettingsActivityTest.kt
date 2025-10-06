package com.saihilg.quizepulse

import org.junit.Assert.*
import org.junit.Test

class SettingsActivityUnitTest {

    @Test
    fun testValidDisplayName() {
        val newName = "John Doe"
        assertTrue(newName.isNotEmpty())
        assertTrue(newName.length >= 1)
    }

    @Test
    fun testInvalidDisplayName() {
        val newName = ""
        assertTrue(newName.isEmpty())
    }

    @Test
    fun testValidPassword() {
        val newPassword = "abcdef"
        assertTrue(newPassword.length >= 6)
    }

    @Test
    fun testInvalidPassword() {
        val newPassword = "123"
        assertFalse(newPassword.length >= 6)
    }

    @Test
    fun testPasswordClearAfterUpdate() {
        // Simulate EditText content
        var etPasswordText = "abcdef"
        // After "update"
        etPasswordText = ""
        assertEquals("", etPasswordText)
    }
}
