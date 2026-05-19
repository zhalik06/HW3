package com.example.hw3.ui1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hw3.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    anime: Anime?,
    onBack: () -> Unit
) {

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
            modifier =
                Modifier
                    .padding(padding)
                    .padding(16.dp)
        ) {

            Button(
                onClick = onBack
            ) {
                Text("Back")
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            if (anime == null) {

                Text(
                    "Anime not found"
                )

            } else {

                Text(
                    anime.title,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    "Year: ${anime.year}"
                )

                Text(
                    "Genre: ${anime.genre}"
                )

                Text(
                    "Episodes: ${anime.episodes}"
                )
            }
        }
    }
}