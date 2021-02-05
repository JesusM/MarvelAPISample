package com.marvelsample.app.ui.characterdetails

data class CharacterModel(
    val name: String,
    val description: String? = null,
    val image: String? = null
)