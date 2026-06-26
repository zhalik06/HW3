package com.example.hw3.data

import com.example.hw3.data.local.FavouriteDao
import com.example.hw3.data.local.FavouriteEntity
import com.example.hw3.model.Anime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouritesRepository @Inject constructor(
    private val dao: FavouriteDao
) {

    fun getAll(): Flow<List<Anime>> =
        dao.getAll().map { list ->
            list.map { it.toAnime() }
        }

    fun isFavourite(id: Int): Flow<Boolean> =
        dao.isFavourite(id)

    suspend fun add(anime: Anime) =
        dao.insert(anime.toEntity())

    suspend fun remove(id: Int) =
        dao.deleteById(id)
}

private fun FavouriteEntity.toAnime() = Anime(
    id = id,
    title = title,
    year = year,
    genre = genre,
    episodes = episodes
)

private fun Anime.toEntity() = FavouriteEntity(
    id = id,
    title = title,
    year = year,
    genre = genre,
    episodes = episodes
)