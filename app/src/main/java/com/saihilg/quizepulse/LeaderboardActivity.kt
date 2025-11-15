package com.saihilg.quizepulse

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.saihilg.quizepulse.adapter.LeaderboardAdapter
import com.saihilg.quizepulse.model.User

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var rvLeaderboard: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var leaderboardListener: ListenerRegistration? = null

    private val userList = mutableListOf<User>()
    private lateinit var adapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        rvLeaderboard = findViewById(R.id.rvLeaderboard)
        rvLeaderboard.layoutManager = LinearLayoutManager(this)

        adapter = LeaderboardAdapter(userList)
        rvLeaderboard.adapter = adapter

        db = FirebaseFirestore.getInstance()
        fetchLeaderboardRealtime()
    }

    private fun fetchLeaderboardRealtime() {
        leaderboardListener = db.collection("users")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(
                        this,
                        "Failed to load leaderboard: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    userList.clear()
                    for (doc in snapshots.documents) {
                        val user = doc.toObject(User::class.java)
                        if (user != null) {
                            userList.add(user)
                        }
                    }

                    // Sort by totalPoints descending
                    userList.sortByDescending { it.totalPoints }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove listener to prevent memory leaks
        leaderboardListener?.remove()
    }
}
