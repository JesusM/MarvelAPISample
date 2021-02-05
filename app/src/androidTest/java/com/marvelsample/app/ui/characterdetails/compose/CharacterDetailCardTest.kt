package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marvelsample.app.ui.characterdetails.CharacterModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplayContent() {
        val expectedName = "name"
        val expectedDescription = "Character description"
        val expectedImage = "a.jpg"
        val element = CharacterModel(expectedName, expectedDescription, expectedImage)

        // Start the app
        composeTestRule.setContent {
            CharacterDetailCard(character = element, modifier = Modifier)
        }

        composeTestRule.onNodeWithContentDescription("Character detail image").assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedName).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Character detail description").assertIsDisplayed()
    }

    @Test
    fun shouldNotDisplayDescriptionIfNotPresent() {
        val expectedName = "name"
        val expectedDescription = null
        val expectedImage = "a.jpg"
        val element = CharacterModel(expectedName, expectedDescription, expectedImage)

        // Start the app
        composeTestRule.setContent {
            CharacterDetailCard(character = element, modifier = Modifier)
        }

        composeTestRule.onNodeWithText(expectedName).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Character detail description").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Character detail image").assertIsDisplayed()
    }
}