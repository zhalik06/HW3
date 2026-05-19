package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    onSearchChange: (String) -> Unit,
    onAnimeClick: (Int) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Anime Viewer")
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
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            uiState.errorMessage?.let {
                Text("Ошибка: $it")
                Spacer(modifier = Modifier.height(8.dp))
            }

            when {

                uiState.isLoading -> {
                    Text("Loading...")
                }

                !uiState.hasSearched -> {
                    Text("Введите название аниме")
                }

                uiState.animeList.isEmpty() -> {
                    Text("Ничего не найдено")
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            uiState.animeList,
                            key = { it.id }
                        ) { anime ->

                            AnimeCard(
                                anime = anime,
                                onClick = {
                                    onAnimeClick(anime.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}