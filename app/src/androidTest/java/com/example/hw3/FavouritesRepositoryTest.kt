package com.example.hw3

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hw3.data.FavouritesRepository
import com.example.hw3.data.local.AppDatabase
import com.example.hw3.model.Anime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouritesRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: FavouritesRepository

    private val anime1 = Anime(1, "Bleach", 2004, "Action", 400)
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = FavouritesRepository(database.favouriteDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addAndGetFavourite() = runTest {
        repository.add(anime1)

        val result = repository.getAll().first()
        assertEquals(1, result.size)
        assertEquals(anime1.title, result[0].title)
        assertEquals(anime1.id, result[0].id)
    }

    @Test
    fun addAndRemoveFavourite() = runTest {
        repository.add(anime1)
        repository.remove(anime1.id)

        val result = repository.getAll().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun isFavouriteAfterAdd() = runTest {
        repository.add(anime1)

        val result = repository.isFavourite(anime1.id).first()
        assertTrue(result)
    }

    @Test
    fun addSameAnimeTwiceNoDuplicate() = runTest {
        repository.add(anime1)
        repository.add(anime1)

        val result = repository.getAll().first()
        assertEquals(1, result.size)
    }

    @Test
    fun animeIsCorrectlyMappedAfterSave() = runTest {
        repository.add(anime1)

        val result = repository.getAll().first()
        val saved = result[0]
        assertEquals(anime1.id, saved.id)
        assertEquals(anime1.title, saved.title)
        assertEquals(anime1.year, saved.year)
        assertEquals(anime1.genre, saved.genre)
        assertEquals(anime1.episodes, saved.episodes)
    }
}