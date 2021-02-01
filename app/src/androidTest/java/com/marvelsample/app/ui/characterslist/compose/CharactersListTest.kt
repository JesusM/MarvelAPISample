package com.marvelsample.app.ui.characterslist.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.paging.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marvelsample.app.ui.base.compose.MainTheme
import com.marvelsample.app.ui.characterslist.ListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldDisplayContent() {
        val expectedName = "name"
        val expectedImage = "a.jpg"

        // Start the app
        composeTestRule.setContent {
            MainTheme {
                val characters =
                    flowOf(PagingData.from(listOf(ListItem(1, expectedName, expectedImage))))
                CharactersList(characters = characters, modifier = Modifier) {}
            }
        }

        composeTestRule.onNodeWithContentDescription("Characters list").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Character $expectedName card.").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayError() {
        composeTestRule.setContent {
            MainTheme {
                CharactersList(characters = createErrorFlow("error"), modifier = Modifier) {}
            }
        }

        composeTestRule.onNodeWithContentDescription("List Error").assertIsDisplayed()
    }

    private fun createErrorFlow(errorMessage: String): Flow<PagingData<ListItem>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 50,
                pageSize = 25
            ),
            pagingSourceFactory = {
                object : PagingSource<Int, ListItem>() {
                    override fun getRefreshKey(state: PagingState<Int, ListItem>): Int? {
                        return null
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListItem> {
                        return LoadResult.Error(throwable = Throwable(errorMessage))
                    }
                }
            }
        ).flow
}