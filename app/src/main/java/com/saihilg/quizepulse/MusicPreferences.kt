package com.saihilg.quizepulse

import android.content.Context

object MusicPreferences {

    private const val PREFS_NAME = "music_prefs"
    private const val KEY_MUSIC_ENABLED = "music_enabled"

    fun isMusicEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_MUSIC_ENABLED, false)  // music OFF by default
    }

    fun setMusicEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply()
    }
}