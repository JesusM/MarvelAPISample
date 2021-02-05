package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.ui.base.compose.MainTheme
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.CharacterModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailBodyTest {
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
                CharacterDetailBody(Result.Success(element), flowOf(PagingData.empty())) {}
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
                CharacterDetailBody(Result.Loading, flowOf(PagingData.empty())) {}
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
                CharacterDetailBody(Result.Error(expectedError), flowOf(PagingData.empty())) {}
            }
        }

        composeTestRule.onNodeWithContentDescription("Error message").assertIsDisplayed()
    }
}