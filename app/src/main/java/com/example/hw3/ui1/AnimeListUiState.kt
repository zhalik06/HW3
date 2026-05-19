package com.example.hw3.ui1

import com.example.hw3.model.Anime

data class AnimeListUiState(

    val searchQuery: String = "",

    val animeList: List<Anime> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val hasSearched: Boolean = false
)