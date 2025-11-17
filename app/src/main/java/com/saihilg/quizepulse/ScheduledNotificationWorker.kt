package com.saihilg.quizepulse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class ScheduledNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val quizMessages = listOf(
        "Time for a quick brain workout! 🧠",
        "Challenge yourself with a quiz! 💡",
        "Keep your mind sharp - Take a quiz! ⚡",
        "Ready for some trivia? Let's go! 🎯",
        "Your daily quiz is waiting! 📚",
        "Test your knowledge now! 🌟",
        "5 minutes to boost your brainpower! 🚀",
        "Quiz time! Show what you know! 🏆",
        "Stay sharp with QuizPulse! 💪",
        "New quiz available - Try it now! 🎓"
    )

    override fun doWork(): Result {

        android.util.Log.d("WorkManager", "ScheduledNotificationWorker is running at ${System.currentTimeMillis()}")

        val channelId = "quizpulse_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "QuizPulse Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Scheduled quiz reminders"
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, QuizSelection::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Random message to make each notification unique
        val randomMessage = quizMessages[Random.nextInt(quizMessages.size)]

        // Use timestamp to ensure unique notification ID
        val notificationId = System.currentTimeMillis().toInt()

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("QuizPulse")
            .setContentText(randomMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(randomMessage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(500, 500, 500))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOnlyAlertOnce(false) // Always alert, don't suppress
            .build()

        notificationManager.notify(notificationId, notification)

        android.util.Log.d("WorkManager", "Notification sent with ID: $notificationId - Message: $randomMessage")

        return Result.success()
    }
}