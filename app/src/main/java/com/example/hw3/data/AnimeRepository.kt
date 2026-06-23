package com.example.hw3.data

import com.example.hw3.data.remote.JikanApi
import com.example.hw3.data.remote.toAnime
import com.example.hw3.data.remote.toDomainOrNull
import com.example.hw3.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AnimeRepository {
    suspend fun searchAnime(query: String): List<Anime>
    suspend fun getAnimeById(id: Int): Anime
}

class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApi
) : AnimeRepository {

    override suspend fun searchAnime(query: String): List<Anime> =
        withContext(Dispatchers.IO) {
            api.searchAnime(query = query)
                .data
                .mapNotNull { it.toDomainOrNull() }
        }

    override suspend fun getAnimeById(id: Int): Anime =
        withContext(Dispatchers.IO) {
            api.getAnimeById(id).data.toAnime()
        }
}