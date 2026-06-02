package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hw3.ui1.AnimeDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    animeId: Int,
    onBack: () -> Unit
) {

    val viewModel: AnimeDetailsViewModel = viewModel()

    val state = viewModel.uiState

    LaunchedEffect(animeId) {
        viewModel.loadAnime(animeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Anime Details")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(onClick = onBack) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {

                state.isLoading -> {
                    Text("Loading...")
                }

                state.errorMessage != null -> {
                    Text("Ошибка: ${state.errorMessage}")
                }

                state.anime != null -> {
                    Text(
                        state.anime.title,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Year: ${state.anime.year ?: "Unknown"}")
                    Text("Genre: ${state.anime.genre}")
                    Text("Episodes: ${state.anime.episodes ?: "Unknown"}")
                }

                else -> {
                    Text("Anime not found")
                }
            }
        }
    }
}