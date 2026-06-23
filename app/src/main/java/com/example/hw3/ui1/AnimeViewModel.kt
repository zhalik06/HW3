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

sealed interface AnimeListUiState {
    data object Initial : AnimeListUiState
    data object Loading : AnimeListUiState
    data class Success(val animeList: List<Anime>) : AnimeListUiState
    data class Error(val message: String) : AnimeListUiState
    data object Empty : AnimeListUiState
}

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    var uiState by mutableStateOf<AnimeListUiState>(AnimeListUiState.Initial)
        private set

    var searchQuery by mutableStateOf("")
        private set

    private var searchJob: Job? = null

    fun onSearchQueryChange(newValue: String) {

        searchQuery = newValue
        searchJob?.cancel()

        val query = newValue.trim()

        if (query.isBlank()) {
            uiState = AnimeListUiState.Initial
            return
        }

        uiState = AnimeListUiState.Loading

        searchJob = viewModelScope.launch {
            delay(500L)

            try {
                val result = repository.searchAnime(query)

                uiState = if (result.isEmpty()) {
                    AnimeListUiState.Empty
                } else {
                    AnimeListUiState.Success(result)
                }

            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                uiState = AnimeListUiState.Error("Ошибка загрузки")
            }
        }
    }

    fun retry() {
        val query = searchQuery.trim()
        if (query.isNotBlank()) {
            uiState = AnimeListUiState.Loading
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                try {
                    val result = repository.searchAnime(query)
                    uiState = if (result.isEmpty()) {
                        AnimeListUiState.Empty
                    } else {
                        AnimeListUiState.Success(result)
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    uiState = AnimeListUiState.Error("Ошибка загрузки")
                }
            }
        }
    }
}