package com.marvelsample.app.ui.characterslist

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.marvelsample.app.R
import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterslist.repository.CharactersListRepository
import com.marvelsample.app.ui.createFakeCharacter
import com.marvelsample.app.ui.launchWithNavigationController
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.not
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
class CharactersListFragmentTest {
    lateinit var mockModule: Module

    @Before
    fun setup() {
        mockModule = module {
            single<CharactersListRepository>(
                named("charactersListRepository"),
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
        var result: Resource<Pager<Character>> = Resource.Error(ResourceError.EmptyContent)
    ) : CharactersListRepository {
        override suspend fun getContent(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> {
            return if (collectionQuery.offset == 0) {
                result
            } else {
                Resource.Error(ResourceError.EmptyContent)
            }
        }
    }

    @Test
    fun shouldDisplayContent() {
        val expectedId = 1
        val expectedElement = createFakeCharacter(expectedId)

        fakeRepository.result = Resource.Success(Pager(listOf(expectedElement)))

        launch().also {
            onView(withId(R.id.characters_list_progress)).check(matches(not(isDisplayed())))
            onView(withId(R.id.characters_list)).check(matches(isDisplayed()))
        }

    }

    @Test
    fun shouldDisplayError() {
        fakeRepository.result = Resource.Error(ResourceError.EmptyContent)

        launch().also {
            onView(withId(R.id.characters_list_progress))
                .check(matches(not(isDisplayed())))
            onView(withId(R.id.characters_list_error_message)).apply {
                check(matches(withText("No content")))
                check(matches(isDisplayed()))
            }
        }

    }

    @Test
    fun shouldNavigateToDetailFromListClick() {
        val expectedId = 1
        val expectedElement = createFakeCharacter(expectedId)

        fakeRepository.result = Resource.Success(Pager(listOf(expectedElement)))

        var navHostController: NavHostController? = null

        // Create a graphical FragmentScenario for the TitleScreen
        launchWithNavigationController(fragmentCreation = {
            navHostController = it
            CharactersListFragment()
        }).also {
            navHostController?.let {
                clickListItem(R.id.characters_list, 0)
                assertEquals(it.currentDestination?.id, R.id.detailsFragment)
            }
        }
    }

    private fun launch(bundle: Bundle = Bundle()): FragmentScenario<CharactersListFragment> =
        launchWithNavigationController(bundle, fragmentCreation = {
            CharactersListFragment()
        })
}