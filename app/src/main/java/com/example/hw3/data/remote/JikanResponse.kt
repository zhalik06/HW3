package com.example.hw3.data.remote

import com.example.hw3.model.Anime
import com.google.gson.annotations.SerializedName

data class JikanSearchResponse(
    val data: List<JikanAnimeDto> = emptyList()
)

data class JikanAnimeDto(

    @SerializedName("mal_id")
    val id: Int,

    val title: String? = null,

    val year: Int? = null,

    val episodes: Int? = null,

    val genres: List<JikanGenreDto>? = null
)

data class JikanGenreDto(
    val name: String? = null
)

fun JikanAnimeDto.toDomainOrNull(): Anime? {

    val safeTitle = title ?: return null

    val genresString =
        genres
            ?.mapNotNull { it.name }
            ?.joinToString(", ")
            .orEmpty()
            .ifBlank { "Unknown" }

    return Anime(
        id = id,
        title = safeTitle,
        year = year ?: 0,
        genre = genresString,
        episodes = episodes ?: 0
    )
}