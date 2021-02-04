package com.marvelsample.app.ui

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import com.marvelsample.app.R
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail

inline fun <reified F : Fragment> launchWithNavigationController(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_MarvelAPISample,
    crossinline fragmentCreation: (NavHostController) -> F,
): FragmentScenario<F> {
    // Create an app TestNavHostController.
    val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )
    UiThreadStatement.runOnUiThread {
        navController.setGraph(R.navigation.navigation)
    }

    // Create fragment scenario.
    val fragmentScenario = launchFragmentInContainer(fragmentArgs, themeResId) {
        fragmentCreation.invoke(navController)
    }

    // Set the NavController property on the fragment.
    fragmentScenario.onFragment { fragment ->
        Navigation.setViewNavController(fragment.requireView(), navController)
    }

    return fragmentScenario
}

inline fun <reified F : Fragment> launchWithNavigationControllerAndCustomToolbar(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_MarvelAPISample,
    crossinline fragmentCreation: (NavHostController) -> F,
): FragmentScenario<F> =
    launchWithNavigationController(fragmentArgs, themeResId,
        fragmentCreation = { navController ->
            fragmentCreation.invoke(navController).also {
                // In addition to returning a new instance of our Fragment, get a callback whenever the
                // fragment’s view is created or destroyed so that we can set the NavController.
                //
                it.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created.
                        Navigation.setViewNavController(it.requireView(), navController)
                    }
                }
            }
        })

fun createEmptyExternalCollection(): ExternalCollection =
    ExternalCollection(0, "", emptyList(), 0)

fun createFakeCharacter(id: Int): Character =
    Character(
        id,
        "name",
        "description",
        Thumbnail("jpg", "a"),
        createEmptyExternalCollection(),
        createEmptyExternalCollection(),
        "modified",
        "resourceURI",
        createEmptyExternalCollection(),
        createEmptyExternalCollection(),
        listOf()
    )