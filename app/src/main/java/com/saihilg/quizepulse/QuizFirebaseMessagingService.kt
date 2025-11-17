package com.saihilg.quizepulse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class QuizFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        android.util.Log.d("FCM", "Message received from: ${remoteMessage.from}")

        // Handle notification payload (works when app is in foreground)
        remoteMessage.notification?.let {
            android.util.Log.d("FCM", "Notification Title: ${it.title}")
            android.util.Log.d("FCM", "Notification Body: ${it.body}")
            sendNotification(it.title ?: "QuizPulse", it.body ?: "Feeling bored? Try a quiz!")
        }

        // Handle data payload (works in all states: foreground, background, killed)
        if (remoteMessage.data.isNotEmpty()) {
            android.util.Log.d("FCM", "Data payload: ${remoteMessage.data}")
            val title = remoteMessage.data["title"] ?: "QuizPulse"
            val message = remoteMessage.data["message"] ?: "Feeling bored? Try a quiz!"
            sendNotification(title, message)
        }
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "quizpulse_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "QuizPulse Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for quiz reminders"
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, QuizSelection::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .build()

        // Use unique ID for each notification
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        android.util.Log.d("FCM", "Notification sent: $title - $message")
    }

   /** override fun onNewToken(token: String) {
        super.onNewToken(token)
        android.util.Log.d("FCM", "New token generated: $token")

    }

    // Optional: Send token to your backend
    private fun sendTokenToServer(token: String) {

    } **/
}