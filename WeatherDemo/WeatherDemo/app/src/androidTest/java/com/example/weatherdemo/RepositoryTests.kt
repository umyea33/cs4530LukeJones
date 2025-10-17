package com.example.weatherdemo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryTests {

    private lateinit var repository: WeatherRepository
    private lateinit var dao: WeatherDAO
    private lateinit var db: WeatherDatabase
    private val scope = CoroutineScope(Job())

    @Before
    fun setup(){
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        dao = db.weatherDao()
        repository = WeatherRepository(scope, dao)
    }

    @Test
    fun `test fetch weather`(): Unit = runBlocking {

        repository.checkWeather("City")
        //use the same scope that the repository "delays" in
        //so that we know that it completes first
        scope.launch {
            assertEquals("City", repository.currentWeather.first().city)
        }
    }
}