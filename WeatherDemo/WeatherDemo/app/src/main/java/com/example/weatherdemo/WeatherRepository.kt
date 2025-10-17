package com.example.weatherdemo


import android.util.Log
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

class WeatherRepository(val scope: CoroutineScope,
                        private val dao: WeatherDAO) {

    //updated when the DB is modified
    val currentWeather: Flow<WeatherData?> = dao.latestWeather()

    //updated with the DB is modified
    val allWeather = dao.allWeather()

    //Make a "web request", then insert it into the DB, triggering the above to update
    fun checkWeather(city: String){
        scope.launch {
            Log.e("REPO", "Fetching weather for $city")
            delay(1000) // pretend this is a slow network call

            val fetchedWeather = WeatherData(Date(), city,
                Random.Default.nextDouble(0.0,105.0))

            //now that we got the weather from our "slow network request" add it to the DB
            dao.addWeatherData(fetchedWeather)
            Log.e("REPO", "told the DAO")
        }
    }
}