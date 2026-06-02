package com.example.hw3.ui1

import com.example.hw3.model.Anime

data class AnimeDetailsUiState(

    val anime: Anime? = null,

    val isLoading: Boolean = false,

    val errorMessage: String? = null
)