package com.marvelsample.app.ui.characterdetails.compose.comics.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marvelsample.app.ui.base.compose.MainTheme
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.characterdetails.comics.compose.CharacterComics
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterComicsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplayCharacterComics() {
        val expectedId = "id"
        val expectedTitle = "Comic title"
        val element = ComicListItem(expectedId, expectedTitle, "a.jpg")

        // Start the app
        composeTestRule.setContent {
            MainTheme {
                CharacterComics(flowOf(PagingData.from(listOf(element))))
            }
        }

        composeTestRule.onNodeWithContentDescription("Characters comics list header").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Character comics list").assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedTitle, useUnmergedTree = true).assertIsDisplayed()
    }
}