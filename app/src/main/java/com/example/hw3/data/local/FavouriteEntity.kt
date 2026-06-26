package com.example.hw3.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val year: Int?,
    val genre: String,
    val episodes: Int?
)