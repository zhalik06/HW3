package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.hw3.model.Anime
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favouritesFlow: StateFlow<List<Anime>>,
    onRemove: (Int) -> Unit,
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = anime.title,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("${anime.year ?: "Unknown"} • ${anime.genre}")
                                Text("Episodes: ${anime.episodes ?: "Unknown"}")
                            }
                            IconButton(onClick = { onRemove(anime.id) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Remove from favourites"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}