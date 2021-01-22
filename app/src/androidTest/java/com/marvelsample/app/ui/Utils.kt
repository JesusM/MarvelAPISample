package com.marvelsample.app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.marvelsample.app.R
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail

inline fun <reified F : Fragment> launch(bundle: Bundle? = null): FragmentScenario<F> =
    launchFragmentInContainer<F>(
        themeResId = R.style.Theme_MarvelAPISample,
        fragmentArgs = bundle
    )

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