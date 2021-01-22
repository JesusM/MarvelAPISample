package com.marvelsample.app.ui.characterslist

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.marvelsample.app.R
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterslist.repository.CharactersListRepository
import com.marvelsample.app.ui.createFakeCharacter
import com.marvelsample.app.ui.launch
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

        launch<CharactersListFragment>()

        onView(withId(R.id.characters_list_progress))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.characters_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayError() {
        fakeRepository.result = Resource.Error(ResourceError.EmptyContent)

        launch<CharactersListFragment>()

        onView(withId(R.id.characters_list_progress))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.characters_list_error_message))
            .check(matches(withText("No content")))
        onView(withId(R.id.characters_list_error_message))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldNavigateToDetailFromListClick() {
        val expectedId = 1
        val expectedElement = createFakeCharacter(expectedId)

        fakeRepository.result = Resource.Success(Pager(listOf(expectedElement)))

        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())
        UiThreadStatement.runOnUiThread {
            navController.setGraph(R.navigation.navigation)
        }

        // Create a graphical FragmentScenario for the TitleScreen
        val charactersListScenario = launch<CharactersListFragment>()

        // Set the NavController property on the fragment
        charactersListScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        clickListItem(R.id.characters_list, 0)
        assertEquals(navController.currentDestination?.id, R.id.detailsFragment)
    }
}