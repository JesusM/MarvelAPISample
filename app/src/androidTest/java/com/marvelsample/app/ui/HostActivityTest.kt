package com.marvelsample.app.ui

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.marvelsample.app.R
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HostActivityTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<HostActivity> =
        ActivityScenarioRule(HostActivity::class.java)

    @Test
    fun shouldInitiallyNavigateToFlightsFragment() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        // Apparently, we need to ensure the graph is applied in the main thread. Otherwise,
        // it will crash :(.
        //
        runOnUiThread {
            navController.setGraph(R.navigation.navigation)
        }

        activityRule.scenario.onActivity { activity ->
            Navigation.setViewNavController(activity.findViewById(R.id.main_content), navController)

            assertEquals(navController.currentDestination?.id, R.id.charactersFragment)
        }
    }
}