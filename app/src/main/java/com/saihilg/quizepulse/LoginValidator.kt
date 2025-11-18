package com.saihilg.quizepulse

object LoginValidator {

    fun validate(email: String, password: String): ValidationResult {
        if (email.isEmpty()) return ValidationResult(false, "Enter email")
        if (password.isEmpty()) return ValidationResult(false, "Enter password")
        if (password.length < 6) return ValidationResult(false, "Password must be at least 6 characters")
        return ValidationResult(true, null)
    }

    data class ValidationResult(val isValid: Boolean, val error: String?)
}
