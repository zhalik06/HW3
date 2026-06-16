package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hw3.ui1.AnimeDetailsUiState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    state: AnimeDetailsUiState,
    isFavouriteFlow: StateFlow<Boolean>,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavourite: () -> Unit
) {
    val isFavourite by isFavouriteFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Details") },
                actions = {
                    if (state is AnimeDetailsUiState.Success) {
                        IconButton(onClick = onToggleFavourite) {
                            Icon(
                                imageVector = if (isFavourite) Icons.Filled.Favorite
                                else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavourite) "Remove from favourites"
                                else "Add to favourites"
                            )
                        }
                    }
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

            when (state) {

                is AnimeDetailsUiState.Initial -> {}

                is AnimeDetailsUiState.Loading -> {
                    Text("Loading...")
                }

                is AnimeDetailsUiState.Error -> {
                    Text("Ошибка: ${state.message}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }

                is AnimeDetailsUiState.Success -> {
                    Text(
                        state.anime.title,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Year: ${state.anime.year ?: "Unknown"}")
                    Text("Genre: ${state.anime.genre}")
                    Text("Episodes: ${state.anime.episodes ?: "Unknown"}")
                }
            }
        }
    }
}