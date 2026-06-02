package com.example.hw3.ui1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw3.NetworkModule
import com.example.hw3.data.AnimeRepository
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val repository: AnimeRepository =
        AnimeRepository(
            NetworkModule.api
        )
) : ViewModel() {

    var uiState by mutableStateOf(
        AnimeDetailsUiState()
    )
        private set

    fun loadAnime(
        animeId: Int
    ) {

        uiState =
            uiState.copy(
                isLoading = true,
                errorMessage = null
            )

        viewModelScope.launch {

            try {

                val anime =
                    repository.getAnimeById(
                        animeId
                    )

                uiState =
                    uiState.copy(
                        anime = anime,
                        isLoading = false
                    )

            } catch (_: Exception) {

                uiState =
                    uiState.copy(
                        isLoading = false,
                        errorMessage = "Ошибка загрузки"
                    )
            }
        }
    }
}