package com.example.hw3.data

import com.example.hw3.NetworkModule
import com.example.hw3.data.remote.toDomainOrNull
import com.example.hw3.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository {

    suspend fun searchAnime(
        query: String
    ): List<Anime> = withContext(Dispatchers.IO) {

        NetworkModule.api
            .searchAnime(query)
            .data
            .mapNotNull {
                it.toDomainOrNull()
            }
    }
}