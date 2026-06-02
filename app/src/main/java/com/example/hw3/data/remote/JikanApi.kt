package com.example.hw3.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApi {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q")
        query: String,

        @Query("limit")
        limit: Int = 25,

        @Query("sfw")
        sfw: Boolean = true
    ): JikanSearchResponse

    @GET("anime/{id}/full")
    suspend fun getAnimeById(
        @Path("id")
        id: Int
    ): JikanAnimeDetailsResponse
}