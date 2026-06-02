package com.example.hw3.data

import com.example.hw3.data.remote.JikanApi
import com.example.hw3.data.remote.toAnime
import com.example.hw3.data.remote.toDomainOrNull
import com.example.hw3.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val api: JikanApi
) {

    suspend fun searchAnime(
        query: String
    ): List<Anime> = withContext(Dispatchers.IO) {

        api.searchAnime(query)
            .data
            .mapNotNull {
                it.toDomainOrNull()
            }
    }

    suspend fun getAnimeById(
        id: Int
    ): Anime = withContext(Dispatchers.IO) {

        api.getAnimeById(id)
            .data
            .toAnime()
    }
}