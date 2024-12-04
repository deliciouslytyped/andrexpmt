package com.example.comp.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.comp.MainActivity
import com.github.takahirom.roborazzi.ROBORAZZI_DEBUG
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w360dp-h740dp-port-xhdpi")
//@LooperMode(LooperMode.Mode.PAUSED) //TODO correctness? //TODO god this effing api
class MainActivityTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        ShadowLog.stream = System.out
    }
    @Test(timeout = 20000)
    fun captureMainScreen() {
        ROBORAZZI_DEBUG = true
        composeRule.mainClock.autoAdvance = false

        // Home screen
        composeRule.mainClock.advanceTimeBy(3_000)
        composeRule.onRoot().captureRoboImage()

        // Navigate to game
        composeRule.onNodeWithText("New Game :D").assertExists()
        composeRule.onNodeWithText("New Game :D").performClick()
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.onRoot().captureRoboImage()

        // Back to home
        pressBack()
        composeRule.mainClock.advanceTimeBy(1_000)

        // Navigate to scores
        composeRule.onNodeWithText("Scores >.>").assertExists()
        composeRule.onNodeWithText("Scores >.>").performClick()
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.onRoot().captureRoboImage()


        // Test Room data insertion
        composeRule.onNodeWithText("Insert Room test data").performClick()
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.onRoot().captureRoboImage()

        /*
        // Switch to Retrofit mode
        composeRule.onNodeWithText("Use retrofit").performClick()
        composeRule.mainClock.advanceTimeBy(500)

        // Enter URL
        composeRule.onNode(hasText("")).performTextInput("http://localhost:8000/")
        composeRule.mainClock.advanceTimeBy(500)
        composeRule.onRoot().captureRoboImage()

        // Test Retrofit data insertion
        composeRule.onNodeWithText("Insert Retrofit test data").performClick()
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.onRoot().captureRoboImage()
        */
    }
}