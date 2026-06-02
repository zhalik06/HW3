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
    onAnimeClick: (Int) -> Unit,
    onRetry: () -> Unit
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


            if (uiState.isLoading) {

                Text("Loading...")

            } else if (uiState.errorMessage != null) {

                Text("Ошибка: ${uiState.errorMessage}")

                Button(onClick = {
                    onRetry()
                }) {
                    Text("Retry")
                }

            } else if (!uiState.hasSearched) {

                Text("Введите название аниме")

            } else if (uiState.animeList.isEmpty()) {

                Text("Ничего не найдено")

            } else {

                LazyColumn {
                    items(uiState.animeList) { anime ->
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