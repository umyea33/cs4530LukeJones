package com.example.gyroscopedemomvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gyroscopedemomvvm.ui.theme.GyroscopeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GyroscopeDemoTheme {
                val myVM: GyroscopeViewModel = viewModel(factory = GyroscopeViewModel.Factory)
                GyroscopeScreen(myVM)
            }
        }
    }
}

@Composable
fun GyroscopeScreen(viewModel: GyroscopeViewModel) {
    val gyroReading by viewModel.gyroReading.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = "Gyro readings:\nx=${gyroReading.x}, y=${gyroReading.y}, z=${gyroReading.z}",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp
        )
    }
}
