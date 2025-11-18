package com.saihilg.quizepulse

object RegisterValidator {

    fun validate(name: String, email: String, password: String): ValidationResult {
        if (name.isEmpty()) return ValidationResult(false, "Enter your name")
        if (email.isEmpty()) return ValidationResult(false, "Enter email")
        if (password.isEmpty() || password.length < 6) return ValidationResult(false, "Password must be at least 6 characters")
        return ValidationResult(true, null)
    }

    data class ValidationResult(val isValid: Boolean, val error: String?)
}
