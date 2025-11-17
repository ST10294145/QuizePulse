package com.saihilg.quizepulse

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    companion object {
        private const val TAG = "MusicService"
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            ACTION_START -> {
                Log.d(TAG, "Starting music")
                startMusic()
            }
            ACTION_STOP -> {
                Log.d(TAG, "Stopping music")
                stopMusic()
                stopSelf() // Stop the service
            }
            else -> {
                // Legacy support for your existing code
                val musicOn = intent?.getBooleanExtra("music_on", false) ?: false
                if (musicOn) {
                    startMusic()
                } else {
                    stopMusic()
                    stopSelf()
                }
            }
        }

        return START_STICKY
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
                mediaPlayer?.isLooping = true
                mediaPlayer?.setVolume(0.5f, 0.5f) // Set volume to 50%
                mediaPlayer?.start()
                Log.d(TAG, "Music started successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error starting music: ${e.message}")
            }
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            Log.d(TAG, "Music resumed")
        }
    }

    private fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
            Log.d(TAG, "Music stopped and released")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
        stopMusic()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}