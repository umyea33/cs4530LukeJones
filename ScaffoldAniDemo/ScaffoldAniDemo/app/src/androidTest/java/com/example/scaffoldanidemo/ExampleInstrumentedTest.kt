package com.example.scaffoldanidemo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDisappearingSection() {
        // Context of the app under test.
        composeTestRule.setContent {
            DisappearingSection()
        }
        composeTestRule.onNodeWithText("Animated Visibility Demo").assertIsDisplayed()
        composeTestRule.mainClock.advanceTimeBy(2500)
        composeTestRule.onNodeWithText("Animated Visibility Demo").assertDoesNotExist()

    }
}