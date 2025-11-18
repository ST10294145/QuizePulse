package com.saihilg.quizepulse

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test for Music Preferences (runs on JVM, no emulator needed)
 *
 * This test validates SharedPreferences logic without requiring Android runtime
 */
@RunWith(MockitoJUnitRunner::class)
class MusicPreferencesUnitTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setUp() {
        // Setup mock behavior
        `when`(mockContext.getSharedPreferences("music_prefs", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor)
        `when`(mockEditor.apply()).then { }
    }

    @Test
    fun testDefaultMusicState() {
        // Mock default value (music OFF)
        `when`(mockSharedPreferences.getBoolean("music_enabled", false))
            .thenReturn(false)

        val isEnabled = MusicPreferences.isMusicEnabled(mockContext)

        assertFalse("Music should be disabled by default", isEnabled)
    }

    @Test
    fun testEnableMusic() {
        MusicPreferences.setMusicEnabled(mockContext, true)

        // Verify that editor methods were called correctly
        verify(mockEditor).putBoolean("music_enabled", true)
        verify(mockEditor).apply()
    }

    @Test
    fun testDisableMusic() {
        MusicPreferences.setMusicEnabled(mockContext, false)

        // Verify that editor methods were called correctly
        verify(mockEditor).putBoolean("music_enabled", false)
        verify(mockEditor).apply()
    }

    @Test
    fun testMusicServiceConstants() {
        // Test that constants are defined correctly
        assertEquals("ACTION_START", MusicService.ACTION_START)
        assertEquals("ACTION_STOP", MusicService.ACTION_STOP)
    }

    @Test
    fun testGetMusicState() {
        // Mock music enabled
        `when`(mockSharedPreferences.getBoolean("music_enabled", false))
            .thenReturn(true)

        val isEnabled = MusicPreferences.isMusicEnabled(mockContext)

        assertTrue("Music should be enabled", isEnabled)
        verify(mockSharedPreferences).getBoolean("music_enabled", false)
    }
}