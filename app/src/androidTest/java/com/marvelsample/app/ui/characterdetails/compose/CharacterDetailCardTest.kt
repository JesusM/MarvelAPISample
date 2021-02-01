package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.ui.base.compose.MainTheme
import com.marvelsample.app.ui.base.model.Result
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
        val expectedDescription = "description"
        val element = CharacterModel(expectedName, expectedDescription, "a.jpg")

        // Start the app
        composeTestRule.setContent {
            MainTheme {
                CharacterDetailCard(characterResult = Result.Success(element), modifier = Modifier)
            }
        }

        composeTestRule.onNodeWithText(expectedName).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedDescription).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Character detail image").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayLoading() {

        // As we are testing a component with animation (progress bar), disable main clock auto
        // advance as otherwise test will fail due to idle timing out.
        //
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            MainTheme {
                CharacterDetailCard(characterResult = Result.Loading, modifier = Modifier)
            }
        }


        composeTestRule.onNodeWithContentDescription("Character loading").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayError() {
        val errorMessage = "a"
        val expectedError = ResourceError.RequestFailError(errorMessage)

        // Start the app
        composeTestRule.setContent {
            MainTheme {
                CharacterDetailCard(
                    characterResult = Result.Error(expectedError),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Error message").assertIsDisplayed()
    }
}