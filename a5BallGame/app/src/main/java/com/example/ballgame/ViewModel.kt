package com.example.ballgame


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.withIndex

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ViewModel(private val repository: GravityRepository) : ViewModel() {

    var maxWidth = 0f
    var maxHeight = 0f

    val gravityReading = repository.getGravityFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GravityReading(0f, 0f, 0f)
        )

    // Variables
    val _position = MutableStateFlow(Pair(0f, 0f))
    val position: StateFlow<Pair<Float, Float>> = _position

    var lastTime = System.currentTimeMillis()
    var velocityX = 0f
    var velocityY = 0f
    var posX = 0f
    var posY = 0f
    val scale = 450

    val friction = 0.95f

    // Launch a coroutine to run when we get new data
    init {
        viewModelScope.launch {
            repository.getGravityFlow().collect { reading ->
                // Calculate time passed
                val currentTime = System.currentTimeMillis()
                val dt = (currentTime - lastTime) / 1000f
                lastTime = currentTime

                // Calculate velocity and position
                velocityX += dt * -scale * reading.x
                velocityY += dt * scale * reading.y
                velocityX *= friction
                velocityY *= friction
                posX += dt * velocityX
                posY += dt * velocityY

                if(posX > maxWidth / 2 - 20)
                    posX = maxWidth / 2 - 20
                else if(posX < -(maxWidth / 2 - 20))
                    posX = -(maxWidth / 2 - 20)

                if(posY > maxHeight / 2 - 20)
                    posY = maxHeight / 2 - 20
                else if(posY < -(maxHeight / 2 - 20))
                    posY = -(maxHeight / 2 - 20)

                // Update the UI
                _position.value = Pair(posX, posY)
            }
        }
    }

    fun setMaxWidthAndHeight(width: Float, height: Float) {
        maxWidth = width
        maxHeight = height
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TheApp)
                ViewModel(application.gravityRepository)
            }
        }
    }
}
