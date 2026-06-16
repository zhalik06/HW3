package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hw3.model.Anime
import com.example.hw3.ui1.widgets.AnimeCard
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favouritesFlow: StateFlow<List<Anime>>,
    onAnimeClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val favourites by favouritesFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Избранное") })
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

            if (favourites.isEmpty()) {
                Text("Избранное пусто")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = favourites,
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