package com.example.weatherdemo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Date


class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    /* here we convert the "cold" flow we get from the DAO into a "hot flow"
      basically for us it means we can efficiently have many subscribers
      in views reading from the flow
     */
    val currentWeather: StateFlow<WeatherData?> =
        repository.currentWeather.stateIn(
            scope = repository.scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = WeatherData(Date(), "nowhere", 0.0)
            )

    val allWeather: StateFlow<List<WeatherData>> = repository.allWeather.stateIn(
        scope = repository.scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf() //start with an empty list
    )

    fun checkWeather(city: String){
        Log.e("VM", "Checking weather $city")
        repository.checkWeather(city)
    }

}

// This factory class allows us to define custom constructors for the view model
/* old version, Java based
class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
*/

//new version using the "view model factory DSL"
object WeatherViewModelProvider {
    val Factory = viewModelFactory {
        // if you had different VM classes, you could add an initailizer block for each one here
        initializer {
            WeatherViewModel(
                //fetches the application singleton
                (this[AndroidViewModelFactory.APPLICATION_KEY]
                        //and then extracts the repository in it
                        as WeatherApplication).weatherRepository
            )
        }
    }
}