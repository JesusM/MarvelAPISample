package com.marvelsample.app.ui.characterdetails

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.marvelsample.app.R
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository
import com.marvelsample.app.ui.createEmptyExternalCollection
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
                fakeRepository
            }
        }

        loadKoinModules(mockModule)
    }

    @After
    fun after() {
        unloadKoinModules(mockModule)
    }

    private val fakeRepository: FakeRepository = FakeRepository()

    private class FakeRepository(
        var result: Resource<Character> = Resource.Error(ResourceError.EmptyContent)
    ) : CharacterDetailsRepository {
        override suspend fun getItem(id: Int): Resource<Character> {
            return result
        }
    }

    @Test
    fun shouldDisplayContent() {
        val expectedId = 1
        val expectedName = "name"
        val expectedDescription = "description"
        val element = Character(
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

        fakeRepository.result = Resource.Success(element)

        launch(Bundle().apply {
            putInt("itemId", expectedId)
        })

        onView(withId(R.id.detail_screen_progress)).check(matches(not(isDisplayed())))

        onView(withId(R.id.detail_screen_character_description)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_screen_character_description)).check(matches(withText(expectedDescription)))

        onView(withId(R.id.detail_screen_character_name)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_screen_character_name)).check(matches(withText(expectedName)))

        onView(withId(R.id.detail_screen_header_image)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayError() {
        val errorMessage = "a"
        fakeRepository.result = Resource.Error(ResourceError.RequestFailError(errorMessage))

        launch(Bundle().apply {
            putInt("itemId", 1)
        })

        onView(withId(R.id.detail_screen_progress)).check(matches(not(isDisplayed())))

        onView(withId(R.id.detail_screen_header_image)).check(matches(not(isDisplayed())))

        onView(withId(R.id.detail_screen_character_name)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_screen_character_name)).check(matches(withText(errorMessage)))

        onView(withId(R.id.detail_screen_character_description)).check(matches(not(isDisplayed())))
    }

    private fun launch(bundle : Bundle) : FragmentScenario<DetailFragment> {
        return com.marvelsample.app.ui.launch(bundle)
    }
}