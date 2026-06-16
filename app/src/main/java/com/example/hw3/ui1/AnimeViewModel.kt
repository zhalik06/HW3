package com.example.hw3.ui1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw3.data.AnimeRepository
import com.example.hw3.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

data class AnimeListUiState(
    val searchQuery: String = "",
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    var uiState by mutableStateOf(AnimeListUiState())
        private set

    private var searchJob: Job? = null

    fun onSearchQueryChange(newValue: String) {

        uiState = uiState.copy(
            searchQuery = newValue,
            errorMessage = null
        )

        searchJob?.cancel()

        val query = newValue.trim()

        if (query.isBlank()) {
            uiState = uiState.copy(
                animeList = emptyList(),
                isLoading = false,
                errorMessage = null,
                hasSearched = false
            )
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

            delay(500)

            uiState = uiState.copy(
                isLoading = true,
                errorMessage = null,
                animeList = emptyList(),
                hasSearched = true
            )

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