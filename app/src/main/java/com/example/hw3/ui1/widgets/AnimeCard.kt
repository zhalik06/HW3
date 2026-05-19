package com.example.hw3.ui1.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hw3.model.Anime

@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = anime.title,
                fontWeight = FontWeight.Bold
            )

            Text("${anime.year} • ${anime.genre}")

            Text("Episodes: ${anime.episodes}")
        }
    }
}