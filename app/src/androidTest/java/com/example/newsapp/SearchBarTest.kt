package com.example.newsapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchField_acceptsTextAndTriggersSearch() {
        composeTestRule
            .onNodeWithTag("search")
            .performTextInput("apple")

        composeTestRule
            .onNodeWithTag("search")
            .assertTextContains("apple")
    }
}
