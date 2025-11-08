package com.example.ballgame

import android.app.Application
import android.content.Context
import android.hardware.SensorManager

class TheApp : Application() {

    val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val gravityRepository by lazy {
        GravityRepository(sensorManager)
    }
}
