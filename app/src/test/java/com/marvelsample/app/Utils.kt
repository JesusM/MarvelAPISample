package com.marvelsample.app

import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail

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

fun createEmptyExternalCollection(): ExternalCollection =
    ExternalCollection(0, "", emptyList(), 0)