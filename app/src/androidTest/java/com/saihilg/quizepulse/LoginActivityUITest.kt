package com.saihilg.quizepulse

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testEmptyEmailShowsError() {
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.et_email)).check(matches(hasErrorText("Enter email")))
    }

    @Test
    fun testShortPasswordShowsError() {
        onView(withId(R.id.et_email)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.et_password)).check(matches(hasErrorText("Password must be at least 6 characters")))
    }
}
