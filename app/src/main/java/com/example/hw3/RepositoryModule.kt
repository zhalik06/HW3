package com.example.hw3

import com.example.hw3.data.AnimeRepository
import com.example.hw3.data.AnimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnimeRepository(
        impl: AnimeRepositoryImpl
    ): AnimeRepository
}