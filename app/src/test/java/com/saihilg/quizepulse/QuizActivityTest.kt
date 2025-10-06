package com.saihilg.quizepulse

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class QuizActivityTest {

    private lateinit var quizQuestions: List<QuizQuestion>

    @Before
    fun setUp() {
        // Simulate football quiz questions
        quizQuestions = listOf(
            QuizQuestion("Who won the 2018 World Cup?", listOf("Brazil", "France", "Germany", "Argentina"), 1),
            QuizQuestion("Which player has the most goals in World Cup history?", listOf("Müller", "Pelé", "Klose", "Messi"), 2)
        )
    }

    @Test
    fun testCorrectAnswerIncreasesScore() {
        var score = 0
        val firstAnswerIndex = 1 // correct
        val secondAnswerIndex = 2 // correct

        if (firstAnswerIndex == quizQuestions[0].correctAnswerIndex) score++
        if (secondAnswerIndex == quizQuestions[1].correctAnswerIndex) score++

        assertEquals(2, score) // Both answers are correct
    }

    @Test
    fun testIncorrectAnswerDoesNotIncreaseScore() {
        var score = 0
        val firstAnswerIndex = 0 // incorrect
        val secondAnswerIndex = 1 // incorrect

        if (firstAnswerIndex == quizQuestions[0].correctAnswerIndex) score++
        if (secondAnswerIndex == quizQuestions[1].correctAnswerIndex) score++

        assertEquals(0, score) // No correct answers
    }

    @Test
    fun testPartialCorrectScore() {
        var score = 0
        val firstAnswerIndex = 1 // correct
        val secondAnswerIndex = 1 // incorrect

        if (firstAnswerIndex == quizQuestions[0].correctAnswerIndex) score++
        if (secondAnswerIndex == quizQuestions[1].correctAnswerIndex) score++

        assertEquals(1, score) // Only first answer correct
    }
}
