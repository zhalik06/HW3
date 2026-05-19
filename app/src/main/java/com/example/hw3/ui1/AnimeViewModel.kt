package com.example.hw3.ui1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw3.data.AnimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AnimeViewModel(
    private val repository: AnimeRepository =
        AnimeRepository()
) : ViewModel() {

    var uiState by mutableStateOf(
        AnimeListUiState()
    )
        private set

    private var searchJob: Job? = null

    fun onSearchQueryChange(
        newValue: String
    ) {

        uiState = uiState.copy(
            searchQuery = newValue,
            errorMessage = null
        )

        searchJob?.cancel()

        val query =
            newValue.trim()

        if (query.isBlank()) {

            uiState =
                uiState.copy(
                    animeList = emptyList(),
                    isLoading = false,
                    errorMessage = null,
                    hasSearched = false
                )

            return
        }

        searchJob =
            viewModelScope.launch {

                delay(500)

                uiState =
                    uiState.copy(
                        isLoading = true
                    )

                try {

                    val result =
                        repository.searchAnime(
                            query
                        )

                    uiState =
                        uiState.copy(
                            animeList = result,
                            isLoading = false,
                            hasSearched = true
                        )

                } catch (
                    e: CancellationException
                ) {

                    throw e

                } catch (
                    _: Exception
                ) {

                    uiState =
                        uiState.copy(
                            isLoading = false,
                            errorMessage = "Ошибка загрузки",
                            hasSearched = true
                        )
                }
            }
    }
}