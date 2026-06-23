package com.example.hw3

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hw3.data.local.AppDatabase
import com.example.hw3.data.local.FavouriteDao
import com.example.hw3.data.local.FavouriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: FavouriteDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.favouriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadFavourite() = runTest {
        val entity = FavouriteEntity(1, "Bleach", 2004, "Action", 400)
        dao.insert(entity)

        val result = dao.getAll().first()
        assertEquals(listOf(entity), result)
    }

    @Test
    fun insertAndDeleteFavourite() = runTest {
        val entity = FavouriteEntity(1, "Bleach", 2004, "Action", 400)
        dao.insert(entity)
        dao.deleteById(1)

        val result = dao.getAll().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertSameIdTwiceNoDuplicate() = runTest {
        val entity1 = FavouriteEntity(1, "Bleach", 2004, "Action", 400)
        val entity2 = FavouriteEntity(1, "Bleach Updated", 2004, "Action", 400)

        dao.insert(entity1)
        dao.insert(entity2)

        val result = dao.getAll().first()
        assertEquals(1, result.size)
        assertEquals("Bleach Updated", result[0].title)
    }

    @Test
    fun favouritesReturnedInAlphabeticalOrder() = runTest {
        val naruto = FavouriteEntity(2, "Naruto", 2002, "Adventure", 220)
        val bleach = FavouriteEntity(1, "Bleach", 2004, "Action", 400)

        dao.insert(naruto)
        dao.insert(bleach)

        val result = dao.getAll().first()
        assertEquals("Bleach", result[0].title)
        assertEquals("Naruto", result[1].title)
    }

    @Test
    fun isFavouriteReturnsTrueAfterInsert() = runTest {
        val entity = FavouriteEntity(1, "Bleach", 2004, "Action", 400)
        dao.insert(entity)

        val result = dao.isFavourite(1).first()
        assertTrue(result)
    }

    @Test
    fun isFavouriteReturnsFalseAfterDelete() = runTest {
        val entity = FavouriteEntity(1, "Bleach", 2004, "Action", 400)
        dao.insert(entity)
        dao.deleteById(1)

        val result = dao.isFavourite(1).first()
        assertFalse(result)
    }
}