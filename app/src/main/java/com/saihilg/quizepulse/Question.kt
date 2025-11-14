package com.saihilg.quizepulse.model

data class Question(
    var questionNumber: Int = 0,
    var text: String = "",
    var options: MutableList<String> = mutableListOf(),
    var answerIndex: Int = 0
)