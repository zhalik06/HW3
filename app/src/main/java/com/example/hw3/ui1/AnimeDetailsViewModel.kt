package com.example.hw3.ui1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw3.data.AnimeRepository
import com.example.hw3.data.FavouritesRepository
import com.example.hw3.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

sealed interface AnimeDetailsUiState {
    data object Initial : AnimeDetailsUiState
    data object Loading : AnimeDetailsUiState
    data class Success(val anime: Anime) : AnimeDetailsUiState
    data class Error(val message: String) : AnimeDetailsUiState
}

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    var uiState by mutableStateOf<AnimeDetailsUiState>(AnimeDetailsUiState.Initial)
        private set

    private val currentAnimeId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isFavourite: StateFlow<Boolean> =
        currentAnimeId
            .flatMapLatest { id ->
                if (id != null) favouritesRepository.isFavourite(id)
                else flowOf(false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )

    fun loadAnime(animeId: Int) {

        if (currentAnimeId.value == animeId && uiState is AnimeDetailsUiState.Success) return

        currentAnimeId.value = animeId
        uiState = AnimeDetailsUiState.Loading

        viewModelScope.launch {
            try {
                val anime = repository.getAnimeById(animeId)
                uiState = AnimeDetailsUiState.Success(anime)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                uiState = AnimeDetailsUiState.Error("Ошибка загрузки")
            }
        }
    }

    fun toggleFavourite(anime: Anime) {
        viewModelScope.launch {
            val fav = favouritesRepository.isFavourite(anime.id).first()
            if (fav) {
                favouritesRepository.remove(anime.id)
            } else {
                favouritesRepository.add(anime)
            }
        }
    }
}