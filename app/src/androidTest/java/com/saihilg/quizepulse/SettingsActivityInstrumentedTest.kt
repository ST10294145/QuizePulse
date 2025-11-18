package com.saihilg.quizepulse

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SettingsActivityInstrumentedTest {

    private lateinit var context: Context
    private lateinit var prefs: android.content.SharedPreferences

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Clear preferences before each test
        prefs.edit().clear().commit()

        // Set default language
        prefs.edit().putString("lang", "en").apply()
    }

    @After
    fun tearDown() {
        // Clean up
        prefs.edit().clear().commit()

        // Stop music service
        val intent = Intent(context, MusicService::class.java)
        intent.action = MusicService.ACTION_STOP
        context.startService(intent)
    }

    @Test
    fun testSettingsActivityLaunches() {
        val scenario = ActivityScenario.launch(SettingsActivity::class.java)
        scenario.onActivity { activity ->
            assertNotNull("Activity should not be null", activity)
        }
        scenario.close()
    }

    @Test
    fun testLanguagePreferencesSaved() {
        // Set language to Afrikaans
        prefs.edit().putString("lang", "af").apply()

        // Verify
        assertEquals("af", prefs.getString("lang", "en"))
    }

    @Test
    fun testLanguagePreferencesDefault() {
        // Clear and check default
        prefs.edit().remove("lang").apply()
        val lang = prefs.getString("lang", "en")

        assertEquals("en", lang)
    }

    @Test
    fun testMusicSwitchStatePreserved() {
        // Turn music on
        prefs.edit().putBoolean("music_on", true).apply()

        // Verify state
        assertTrue(prefs.getBoolean("music_on", false))

        // Turn music off
        prefs.edit().putBoolean("music_on", false).apply()

        // Verify state
        assertFalse(prefs.getBoolean("music_on", false))
    }

    @Test
    fun testMultipleLanguageSwitches() {
        // English
        prefs.edit().putString("lang", "en").apply()
        assertEquals("en", prefs.getString("lang", "en"))

        // Afrikaans
        prefs.edit().putString("lang", "af").apply()
        assertEquals("af", prefs.getString("lang", "en"))

        // Zulu
        prefs.edit().putString("lang", "zu").apply()
        assertEquals("zu", prefs.getString("lang", "en"))

        // Back to English
        prefs.edit().putString("lang", "en").apply()
        assertEquals("en", prefs.getString("lang", "en"))
    }

    @Test
    fun testMusicAndLanguageIndependent() {
        // Set both preferences
        prefs.edit()
            .putString("lang", "af")
            .putBoolean("music_on", true)
            .apply()

        // Verify both
        assertEquals("af", prefs.getString("lang", "en"))
        assertTrue(prefs.getBoolean("music_on", false))

        // Change only language
        prefs.edit().putString("lang", "zu").apply()

        // Music should remain unchanged
        assertTrue(prefs.getBoolean("music_on", false))
        assertEquals("zu", prefs.getString("lang", "en"))
    }

    @Test
    fun testContextIsValid() {
        assertNotNull("Context should not be null", context)
        assertEquals("com.saihilg.quizepulse", context.packageName)
    }

    @Test
    fun testSharedPreferencesPersistence() {
        // Save preferences
        prefs.edit()
            .putString("lang", "zu")
            .putBoolean("music_on", true)
            .commit()

        // Get new instance
        val newPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Verify persistence
        assertEquals("zu", newPrefs.getString("lang", "en"))
        assertTrue(newPrefs.getBoolean("music_on", false))
    }

    @Test
    fun testFirebaseAuthInstance() {
        val auth = FirebaseAuth.getInstance()
        assertNotNull("FirebaseAuth should not be null", auth)
    }
}