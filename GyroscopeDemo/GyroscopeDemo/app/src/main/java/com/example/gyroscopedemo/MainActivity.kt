package com.example.gyroscopedemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gyroscopedemo.ui.theme.GyroscopeDemoTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GyroscopeDemoTheme {
               Gyroscope ()
            }
        }
    }
}

@Composable
fun Gyroscope() {

    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    if (gyroscope == null) {
        Text("No gyroscope available on this device")
        return
    }

    val gyroFlow = sensorManager.gyroFlow(gyroscope)
    val gyroReading by gyroFlow.collectAsStateWithLifecycle(GyroReading(0f, 0f, 0f))

    Surface(modifier = Modifier.fillMaxSize().padding(10.dp), color = MaterialTheme.colorScheme.background) {
        Text("Gyro reading: ${gyroReading.x}, ${gyroReading.y}, ${gyroReading.z}", fontSize = 20.sp)
    }
}

data class GyroReading(val x: Float, val y: Float, val z: Float)

fun SensorManager.gyroFlow(gyroscope: Sensor): Flow<GyroReading> = channelFlow {
    val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            trySendBlocking(GyroReading(event.values[0], event.values[1], event.values[2]))
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }
    registerListener(listener, gyroscope, SensorManager.SENSOR_DELAY_UI)
    awaitClose { unregisterListener(listener) }
}


