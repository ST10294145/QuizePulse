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
class RegisterActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun elementsAreVisible() {
        onView(withId(R.id.et_name)).check(matches(isDisplayed()))
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_signup)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToLogin_onSubtitleClick() {
        onView(withId(R.id.tv_subtitle)).perform(click())
    }

    @Test
    fun registerButton_showsValidationError_onEmptyFields() {
        onView(withId(R.id.btn_signup)).perform(click())
        onView(withId(R.id.et_name)).check(matches(hasErrorText("Enter your name")))
    }

    @Test
    fun enterValidData_clickRegisterButton() {
        onView(withId(R.id.et_name)).perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.et_email)).perform(typeText("john@email.com"), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.btn_signup)).perform(click())
    }
}
