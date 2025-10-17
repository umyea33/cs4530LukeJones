package com.example.weatherdemo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.util.Date

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DAOTests {

    private lateinit var dao: WeatherDAO
    private lateinit var db: WeatherDatabase


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        dao = db.weatherDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }



    @Test
    fun `start with an empty db`() = runBlocking{
        assertTrue(dao.allWeather().first().isEmpty())
    }

    @Test
    fun `test one update`() = runBlocking {
        val now = Date()
        dao.addWeatherData(WeatherData(now, "city", 3.5))
        val expected = WeatherData(now, "city", 3.5)
        assertEquals(expected, dao.latestWeather().first())
        assertEquals(expected, dao.allWeather().first()[0])
        assertEquals(1, dao.allWeather().first().size)
    }
}