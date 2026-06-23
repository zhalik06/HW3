package com.example.hw3

import com.example.hw3.data.AnimeRepository
import com.example.hw3.model.Anime
import com.example.hw3.ui1.AnimeDetailsUiState
import com.example.hw3.ui1.AnimeDetailsViewModel
import com.example.hw3.ui1.AnimeListUiState
import com.example.hw3.ui1.AnimeViewModel
import com.example.hw3.data.FavouritesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnimeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AnimeRepository = mockk()

    private val anime1 = Anime(1, "Bleach", 2004, "Action", 400)
    private val anime2 = Anime(2, "Naruto", 2002, "Adventure", 220)

    @Before
    fun setUp() {
        coEvery { repository.searchAnime(any()) } returns emptyList()
    }

    @Test
    fun `initial state is Initial`() = runTest {
        val vm = AnimeViewModel(repository)
        assertTrue(vm.uiState is AnimeListUiState.Initial)
        assertEquals("", vm.searchQuery)
    }

    @Test
    fun `search success shows Success state`() = runTest {
        coEvery { repository.searchAnime("bleach") } returns listOf(anime1)
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("bleach")
        advanceTimeBy(600)
        advanceUntilIdle()

        val state = vm.uiState
        assertTrue(state is AnimeListUiState.Success)
        assertEquals(listOf(anime1), (state as AnimeListUiState.Success).animeList)
    }

    @Test
    fun `search failure shows Error state`() = runTest {
        coEvery { repository.searchAnime("bleach") } throws RuntimeException("Network error")
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("bleach")
        advanceTimeBy(600)
        advanceUntilIdle()

        val state = vm.uiState
        assertTrue(state is AnimeListUiState.Error)
        assertEquals("Ошибка загрузки", (state as AnimeListUiState.Error).message)
    }

    @Test
    fun `retry after error initiates new request`() = runTest {
        coEvery { repository.searchAnime("bleach") } throws RuntimeException("error")
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("bleach")
        advanceTimeBy(600)
        advanceUntilIdle()

        assertTrue(vm.uiState is AnimeListUiState.Error)

        coEvery { repository.searchAnime("bleach") } returns listOf(anime1)
        vm.retry()
        advanceUntilIdle()

        assertTrue(vm.uiState is AnimeListUiState.Success)
        coVerify(exactly = 2) { repository.searchAnime("bleach") }
    }

    @Test
    fun `empty search result shows Empty state`() = runTest {
        coEvery { repository.searchAnime("xyz") } returns emptyList()
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("xyz")
        advanceTimeBy(600)
        advanceUntilIdle()

        assertTrue(vm.uiState is AnimeListUiState.Empty)
    }

    @Test
    fun `blank query resets to Initial state`() = runTest {
        coEvery { repository.searchAnime("bleach") } returns listOf(anime1)
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("bleach")
        advanceTimeBy(600)
        advanceUntilIdle()

        vm.onSearchQueryChange("")
        advanceUntilIdle()

        assertTrue(vm.uiState is AnimeListUiState.Initial)
    }

    @Test
    fun `new query cancels previous search job`() = runTest {
        coEvery { repository.searchAnime("b") } returns listOf(anime1)
        coEvery { repository.searchAnime("bleach") } returns listOf(anime2)
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("b")
        advanceTimeBy(300) // debounce ещё не прошёл
        vm.onSearchQueryChange("bleach")
        advanceTimeBy(600)
        advanceUntilIdle()

        val state = vm.uiState as AnimeListUiState.Success
        assertEquals(listOf(anime2), state.animeList)
        coVerify(exactly = 0) { repository.searchAnime("b") }
    }

    @Test
    fun `retry calls repository again`() = runTest {
        coEvery { repository.searchAnime("naruto") } returns listOf(anime2)
        val vm = AnimeViewModel(repository)

        vm.onSearchQueryChange("naruto")
        advanceTimeBy(600)
        advanceUntilIdle()

        vm.retry()
        advanceUntilIdle()

        coVerify(exactly = 2) { repository.searchAnime("naruto") }
    }
}