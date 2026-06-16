package com.example.hw3.data.remote

import com.example.hw3.model.Anime
import com.google.gson.annotations.SerializedName

data class JikanAnimeDetailsResponse(
    val data: JikanAnimeDetailsDto
)

data class JikanAnimeDetailsDto(

    @SerializedName("mal_id")
    val id: Int,

    val title: String?,

    val year: Int?,

    val episodes: Int?,

    val genres: List<JikanGenreDto>?
)

fun JikanAnimeDetailsDto.toAnime(): Anime {

    return Anime(
        id = id,
        title = title ?: "Unknown",
        year = year,
        genre = genres
            ?.mapNotNull { it.name }
            ?.joinToString(", ")
            .orEmpty()
            .ifBlank { "Unknown" },
        episodes = episodes
    )
}