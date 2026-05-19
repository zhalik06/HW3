package com.example.hw3

object Routes {

    const val LIST = "list"
    const val DETAILS = "details"
    const val ANIME_ID = "animeId"

    const val DETAILS_ROUTE = "$DETAILS/{$ANIME_ID}"

    fun details(id: Int) = "$DETAILS/$id"
}