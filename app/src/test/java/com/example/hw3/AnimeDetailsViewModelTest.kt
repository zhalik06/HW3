package com.example.hw3

import com.example.hw3.data.AnimeRepository
import com.example.hw3.data.FavouritesRepository
import com.example.hw3.model.Anime
import com.example.hw3.ui1.AnimeDetailsUiState
import com.example.hw3.ui1.AnimeDetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.flow.first

class AnimeDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AnimeRepository = mockk()
    private val favouritesRepository: FavouritesRepository = mockk()

    private val anime = Anime(1, "Bleach", 2004, "Action", 400)

    @Test
    fun `initial state is Initial`() = runTest {
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)
        assertTrue(vm.uiState is AnimeDetailsUiState.Initial)
    }

    @Test
    fun `loadAnime success shows Success state`() = runTest {
        coEvery { repository.getAnimeById(1) } returns anime
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)

        vm.loadAnime(1)
        advanceUntilIdle()

        val state = vm.uiState
        assertTrue(state is AnimeDetailsUiState.Success)
        assertEquals(anime, (state as AnimeDetailsUiState.Success).anime)
    }

    @Test
    fun `loadAnime failure shows Error state`() = runTest {
        coEvery { repository.getAnimeById(1) } throws RuntimeException("error")
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)

        vm.loadAnime(1)
        advanceUntilIdle()

        assertTrue(vm.uiState is AnimeDetailsUiState.Error)
        assertEquals("Ошибка загрузки", (vm.uiState as AnimeDetailsUiState.Error).message)
    }

    @Test
    fun `loadAnime with same id does not reload if already Success`() = runTest {
        coEvery { repository.getAnimeById(1) } returns anime
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)

        vm.loadAnime(1)
        advanceUntilIdle()
        vm.loadAnime(1)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.getAnimeById(1) }
    }

    @Test
    fun `isFavourite returns correct flow value`() = runTest {
        coEvery { favouritesRepository.isFavourite(1) } returns flowOf(true)
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)

        val result = favouritesRepository.isFavourite(1).first()

        assertTrue(result)
    }

    @Test
    fun `toggleFavourite adds anime if not favourite`() = runTest {
        coEvery { favouritesRepository.isFavourite(1) } returns flowOf(false)
        coEvery { favouritesRepository.add(anime) } returns Unit
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)

        vm.toggleFavourite(anime)
        advanceUntilIdle()

        coVerify(exactly = 1) { favouritesRepository.add(anime) }
    }

    @Test
    fun `toggleFavourite removes anime if already favourite`() = runTest {
        coEvery { favouritesRepository.isFavourite(1) } returns flowOf(true)
        coEvery { favouritesRepository.remove(1) } returns Unit
        val vm = AnimeDetailsViewModel(repository, favouritesRepository)

        vm.toggleFavourite(anime)
        advanceUntilIdle()

        coVerify(exactly = 1) { favouritesRepository.remove(1) }
    }
}