package com.saihilg.quizepulse.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.saihilg.quizepulse.model.Question

class QuestionRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getQuestions(
        quizType: String,    // e.g., "football"
        difficulty: String,  // e.g., "easy" or "hard"
        callback: (List<Question>) -> Unit
    ) {
        db.collection("quizzes")
            .document(quizType.lowercase())       // force lowercase
            .collection(difficulty.lowercase())   // force lowercase
            .get()
            .addOnSuccessListener { documents ->
                // Map documents to Question objects
                val questions = documents.mapNotNull { it.toObject(Question::class.java) }
                    // Sort by questionNumber
                    .sortedBy { it.questionNumber }

                callback(questions)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                callback(emptyList())
            }
    }
}
