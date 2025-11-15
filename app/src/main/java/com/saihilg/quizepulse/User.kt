package com.saihilg.quizepulse.model

import com.google.firebase.Timestamp

data class User(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var totalPoints: Int = 0,
    var createdAt: Timestamp? = null
)
