package com.example.gyroscopedemomvvm


import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope


class GyroscopeViewModel(private val repository: GyroscopeRepository) : ViewModel() {

    val gyroReading = repository.getGyroFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GyroReading(0f, 0f, 0f)
        )

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GyroscopeApp)
                GyroscopeViewModel(application.gyroscopeRepository)
            }
        }
    }
}
