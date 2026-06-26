package com.example.hw3

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hw3.ui1.AnimeListUiState
import com.example.hw3.ui1.screens.AnimeListScreen
import com.example.hw3.model.Anime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimeListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val anime = Anime(1, "Bleach", 2004, "Action", 400)

    @Test
    fun showsLoadingState() {
        composeRule.setContent {
            AnimeListScreen(
                uiState = AnimeListUiState.Loading,
                searchQuery = "bleach",
                onSearchChange = {},
                onAnimeClick = {},
                onRetry = {},
                onFavouritesClick = {}
            )
        }
        composeRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun showsAnimeListOnSuccess() {
        composeRule.setContent {
            AnimeListScreen(
                uiState = AnimeListUiState.Success(listOf(anime)),
                searchQuery = "bleach",
                onSearchChange = {},
                onAnimeClick = {},
                onRetry = {},
                onFavouritesClick = {}
            )
        }
        composeRule.onNodeWithText("Bleach").assertIsDisplayed()
    }

    @Test
    fun showsRetryButtonOnError() {
        composeRule.setContent {
            AnimeListScreen(
                uiState = AnimeListUiState.Error("Ошибка загрузки"),
                searchQuery = "bleach",
                onSearchChange = {},
                onAnimeClick = {},
                onRetry = {},
                onFavouritesClick = {}
            )
        }
        composeRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun retryButtonCallsOnRetry() {
        var retryClicked = false
        composeRule.setContent {
            AnimeListScreen(
                uiState = AnimeListUiState.Error("Ошибка загрузки"),
                searchQuery = "bleach",
                onSearchChange = {},
                onAnimeClick = {},
                onRetry = { retryClicked = true },
                onFavouritesClick = {}
            )
        }
        composeRule.onNodeWithText("Retry").performClick()
        assert(retryClicked)
    }
}