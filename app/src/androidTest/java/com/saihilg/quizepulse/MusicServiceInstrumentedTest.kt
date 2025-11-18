package com.saihilg.quizepulse

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for Music Service functionality
 *
 * This test validates that:
 * 1. SharedPreferences correctly store music state
 * 2. Music service can be started and stopped
 * 3. Settings persist across app sessions
 */
@RunWith(AndroidJUnit4::class)
class MusicServiceInstrumentedTest {

    private lateinit var context: Context
    private lateinit var prefs: android.content.SharedPreferences

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Clear preferences before each test
        prefs.edit().clear().commit()
    }

    @After
    fun tearDown() {
        // Clean up after tests
        prefs.edit().clear().commit()

        // Stop music service if running
        val intent = Intent(context, MusicService::class.java)
        intent.action = MusicService.ACTION_STOP
        context.startService(intent)
    }

    @Test
    fun testMusicPreferencesDefaultValue() {
        // Test that music is OFF by default
        val musicOn = prefs.getBoolean("music_on", false)
        assertFalse("Music should be OFF by default", musicOn)
    }

    @Test
    fun testMusicPreferencesSaveAndRetrieve() {
        // Save music preference as ON
        prefs.edit().putBoolean("music_on", true).apply()

        // Retrieve and verify
        val musicOn = prefs.getBoolean("music_on", false)
        assertTrue("Music preference should be saved as ON", musicOn)
    }

    @Test
    fun testMusicPreferencesToggle() {
        // Start with OFF
        prefs.edit().putBoolean("music_on", false).apply()
        assertFalse(prefs.getBoolean("music_on", false))

        // Toggle to ON
        prefs.edit().putBoolean("music_on", true).apply()
        assertTrue(prefs.getBoolean("music_on", false))

        // Toggle back to OFF
        prefs.edit().putBoolean("music_on", false).apply()
        assertFalse(prefs.getBoolean("music_on", false))
    }

    @Test
    fun testMusicServiceStartAction() {
        // Create intent with START action
        val intent = Intent(context, MusicService::class.java)
        intent.action = MusicService.ACTION_START

        // Start service (should not crash)
        val component = context.startService(intent)
        assertNotNull("Service should start successfully", component)

        // Clean up
        Thread.sleep(500) // Give service time to start
        val stopIntent = Intent(context, MusicService::class.java)
        stopIntent.action = MusicService.ACTION_STOP
        context.startService(stopIntent)
    }

    @Test
    fun testMusicServiceStopAction() {
        // Start music first
        val startIntent = Intent(context, MusicService::class.java)
        startIntent.action = MusicService.ACTION_START
        context.startService(startIntent)

        Thread.sleep(500)

        // Stop music
        val stopIntent = Intent(context, MusicService::class.java)
        stopIntent.action = MusicService.ACTION_STOP
        val component = context.startService(stopIntent)

        assertNotNull("Service should handle stop action", component)
    }

    @Test
    fun testMusicServiceLegacyMode() {
        // Test legacy mode with music_on = true
        val intent = Intent(context, MusicService::class.java)
        intent.putExtra("music_on", true)

        val component = context.startService(intent)
        assertNotNull("Service should start with legacy mode", component)

        Thread.sleep(500)

        // Stop service
        val stopIntent = Intent(context, MusicService::class.java)
        stopIntent.putExtra("music_on", false)
        context.startService(stopIntent)
    }

    @Test
    fun testMusicPreferencesObject() {
        // Test MusicPreferences helper object
        MusicPreferences.setMusicEnabled(context, true)
        assertTrue("MusicPreferences should save enabled state",
            MusicPreferences.isMusicEnabled(context))

        MusicPreferences.setMusicEnabled(context, false)
        assertFalse("MusicPreferences should save disabled state",
            MusicPreferences.isMusicEnabled(context))
    }

    @Test
    fun testMultipleServiceStarts() {
        // Test that multiple start commands don't cause issues
        val intent = Intent(context, MusicService::class.java)
        intent.action = MusicService.ACTION_START

        context.startService(intent)
        Thread.sleep(200)

        context.startService(intent)
        Thread.sleep(200)

        context.startService(intent)
        Thread.sleep(200)

        // Should not crash - stop service
        val stopIntent = Intent(context, MusicService::class.java)
        stopIntent.action = MusicService.ACTION_STOP
        val component = context.startService(stopIntent)

        assertNotNull("Service should handle multiple starts gracefully", component)
    }

    @Test
    fun testContextIsValid() {
        // Verify we have valid context
        assertNotNull("Context should not be null", context)
        assertEquals("Context should be application context",
            "com.saihilg.quizepulse", context.packageName)
    }

    @Test
    fun testSharedPreferencesPersistence() {
        // Save preference
        prefs.edit().putBoolean("music_on", true).commit()

        // Get new instance of SharedPreferences
        val newPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Verify persistence
        assertTrue("Preferences should persist across instances",
            newPrefs.getBoolean("music_on", false))
    }

    @Test
    fun testLanguagePreferencesDoNotAffectMusic() {
        // Set language preference
        prefs.edit().putString("lang", "af").apply()

        // Set music preference
        prefs.edit().putBoolean("music_on", true).apply()

        // Verify both are saved correctly
        assertEquals("Language should be Afrikaans", "af", prefs.getString("lang", "en"))
        assertTrue("Music should be ON", prefs.getBoolean("music_on", false))
    }

    @Test
    fun testServiceConstants() {
        // Verify action constants exist and are correct
        assertEquals("ACTION_START constant should match",
            "ACTION_START", MusicService.ACTION_START)
        assertEquals("ACTION_STOP constant should match",
            "ACTION_STOP", MusicService.ACTION_STOP)
    }
}