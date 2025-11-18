package com.saihilg.quizepulse

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.junit.Assert.*

class MusicPreferencesTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        context = mock(Context::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor)
    }

    @Test
    fun testSetMusicEnabled() {
        MusicPreferences.setMusicEnabled(context, true)
        verify(editor).putBoolean("music_enabled", true)
        verify(editor).apply()
    }

    @Test
    fun testIsMusicEnabled() {
        `when`(sharedPreferences.getBoolean("music_enabled", false)).thenReturn(true)
        val result = MusicPreferences.isMusicEnabled(context)
        assertTrue(result)
    }
}
