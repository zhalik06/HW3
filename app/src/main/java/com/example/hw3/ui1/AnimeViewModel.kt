package com.example.hw3.ui1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw3.NetworkModule
import com.example.hw3.data.AnimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AnimeViewModel(
    private val repository: AnimeRepository =
        AnimeRepository(NetworkModule.api)
) : ViewModel() {

    var uiState by mutableStateOf(AnimeListUiState())
        private set

    private var searchJob: Job? = null

    fun onSearchQueryChange(newValue: String) {

        uiState = uiState.copy(
            searchQuery = newValue
        )

        val query = newValue.trim()

        if (query.isBlank()) {
            searchJob?.cancel()
            uiState = AnimeListUiState()
            return
        }

        loadAnime(query)
    }

    fun retry() {
        val query = uiState.searchQuery.trim()

        if (query.isNotBlank()) {
            loadAnime(query)
        }
    }

    private fun loadAnime(query: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {

            uiState = uiState.copy(
                isLoading = true,
                errorMessage = null,
                animeList = emptyList(),
                hasSearched = true
            )

            delay(500)

            try {

                val result = repository.searchAnime(query)

                uiState = uiState.copy(
                    animeList = result,
                    isLoading = false,
                    errorMessage = null
                )

            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {

                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Ошибка загрузки",
                    animeList = emptyList()
                )
            }
        }
    }
}