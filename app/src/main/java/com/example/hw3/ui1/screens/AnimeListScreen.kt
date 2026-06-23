package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hw3.ui1.AnimeListUiState
import com.example.hw3.ui1.widgets.AnimeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    uiState: AnimeListUiState,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAnimeClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onFavouritesClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Viewer") },
                actions = {
                    IconButton(onClick = onFavouritesClick) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favourites"
                        )
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {

                is AnimeListUiState.Initial -> {
                    Text("Введите название аниме")
                }

                is AnimeListUiState.Loading -> {
                    Text("Loading...")
                }

                is AnimeListUiState.Error -> {
                    Text("Ошибка: ${uiState.message}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }

                is AnimeListUiState.Empty -> {
                    Text("Ничего не найдено")
                }

                is AnimeListUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.animeList,
                            key = { it.id }
                        ) { anime ->
                            AnimeCard(
                                anime = anime,
                                onClick = { onAnimeClick(anime.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}