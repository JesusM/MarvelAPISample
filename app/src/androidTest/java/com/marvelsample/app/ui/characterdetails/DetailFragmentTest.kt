package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.marvelsample.app.R
import com.marvelsample.app.core.model.ExternalItem
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.model.comic.Comic
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterdetails.comics.repository.CharacterComicsRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository
import com.marvelsample.app.ui.createEmptyExternalCollection
import com.marvelsample.app.ui.launchWithNavigationControllerAndCustomToolbar
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@LargeTest
class DetailFragmentTest {
    lateinit var mockModule: Module

    @Before
    fun setup() {
        mockModule = module {
            single<CharacterDetailsRepository>(
                named("characterDetailsRepository"),
                override = true
            ) {
                fakeDetailsRepository
            }

            single<CharacterComicsRepository>(
                named("characterComicsRepository"),
                override = true
            ) {
                fakeComicsRepository
            }
        }

        loadKoinModules(mockModule)
    }

    @After
    fun after() {
        unloadKoinModules(mockModule)
    }

    private val fakeDetailsRepository = FakeDetailsRepository()

    private class FakeDetailsRepository(
        var result: Resource<Character> = Resource.Error(ResourceError.EmptyContent)
    ) : CharacterDetailsRepository {
        override suspend fun getItem(id: Int): Resource<Character> {
            return result
        }
    }

    @Test
    fun shouldDisplayDetails() {
        val expectedId = 1
        val expectedName = "name"
        val expectedDescription = "description"
        val element = createFakeDetails(expectedId, expectedName, expectedDescription)

        fakeDetailsRepository.result = Resource.Success(element)

        launch(Bundle().apply {
            putInt("itemId", expectedId)
        }).also {
            onView(withId(R.id.detail_screen_progress)).check(matches(not(isDisplayed())))

            onView(withId(R.id.detail_screen_character_description)).apply {
                check(matches(isDisplayed()))
                check(matches(withText(expectedDescription)))
            }

            onView(withId(R.id.detail_screen_character_name)).apply {
                check(matches(isDisplayed()))
                check(matches(withText(expectedName)))
            }

            onView(withId(R.id.detail_screen_header_image)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayError() {
        val errorMessage = "a"
        fakeDetailsRepository.result = Resource.Error(ResourceError.RequestFailError(errorMessage))

        launch(Bundle().apply {
            putInt("itemId", 1)
        }).also {
            onView(withId(R.id.detail_screen_progress)).check(matches(not(isDisplayed())))

            onView(withId(R.id.detail_screen_header_image)).check(matches(not(isDisplayed())))

            onView(withId(R.id.detail_screen_character_name)).apply {
                check(matches(isDisplayed()))
                check(matches(withText(errorMessage)))
            }

            onView(withId(R.id.detail_screen_character_description)).check(matches(not(isDisplayed())))
        }
    }

    private fun launch(bundle: Bundle): FragmentScenario<DetailFragment> =
        launchWithNavigationControllerAndCustomToolbar(
            bundle, fragmentCreation = {
                DetailFragment()
            })

    private fun createFakeDetails(
        expectedId: Int = 1,
        expectedName: String = "name",
        expectedDescription: String = "description"
    ): Character {
        return Character(
            expectedId,
            expectedName,
            expectedDescription,
            Thumbnail("jpg", "a"),
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            "",
            "",
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            emptyList()
        )
    }

    //region Comics

    @Test
    fun shouldDisplayComics() {
        val expectedCharacterId = 1
        val expectedComicId = "1"
        val expectedComicTitle = "title"
        fakeComicsRepository.result = Resource.Success(
            Pager(
                listOf(
                    createFakeComics(
                        expectedId = expectedComicId,
                        expectedName = expectedComicTitle
                    )
                )
            )
        )

        launch(Bundle().apply {
            putInt("itemId", expectedCharacterId)
        }).also {
            onView(withId(R.id.horizontal_list_progress)).check(matches(not(isDisplayed())))
            onView(withId(R.id.horizontal_list)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldNotDisplayComicsIfError() {
        val expectedCharacterId = 1
        fakeComicsRepository.result = Resource.Error(ResourceError.EmptyContent)

        launch(Bundle().apply {
            putInt("itemId", expectedCharacterId)
        }).also {
            onView(withId(R.id.horizontal_list_progress)).check(matches(not(isDisplayed())))
            onView(withId(R.id.horizontal_list)).check(matches(not(isDisplayed())))
        }
    }

    private val fakeComicsRepository = FakeComicsRepository()

    private class FakeComicsRepository(
        var result: Resource<Pager<Comic>> = Resource.Error(ResourceError.EmptyContent)
    ) : CharacterComicsRepository {
        override suspend fun getComics(
            id: Int,
            collectionQuery: CollectionRequestParams
        ): Resource<Pager<Comic>> {
            return result
        }
    }

    private fun createFakeComics(
        expectedId: String = "1",
        expectedName: String = "name",
        thumbnail: Thumbnail = Thumbnail("jpg", "a")
    ): Comic {
        return Comic(
            expectedId,
            "",
            expectedName,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            emptyList(),
            "",
            emptyList(),
            ExternalItem("", ""),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            thumbnail,
            emptyList(),
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            createEmptyExternalCollection()
        )
    }

    //endregion
}