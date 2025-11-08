package com.example.ballgame

import android.annotation.SuppressLint
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
import com.example.ballgame.ui.theme.GyroscopeDemoTheme

// My imports
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GyroscopeDemoTheme {
                val myVM: ViewModel = viewModel(factory = ViewModel.Factory)
                GravityScreen(myVM)
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GravityScreen(viewModel: ViewModel) {
    val position by viewModel.position.collectAsStateWithLifecycle()
    val (posX, posY) = position

    BoxWithConstraints (
        modifier = Modifier.fillMaxSize()
    ) {
        val centerX = maxWidth / 2
        val centerY = maxHeight / 2

        viewModel.setMaxWidthAndHeight(maxWidth.value, maxHeight.value)

        Box (
            modifier = Modifier
                .offset(centerX - 20.dp + viewModel.posX.dp, centerY - 20.dp + viewModel.posY.dp)
                .size(40.dp)
                .background(Color.Blue, CircleShape)
        ) {

        }
    }

//    Surface(
//        modifier = Modifier.fillMaxSize().padding(10.dp),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Text(
//            text = "Gravity readings:\nx=${gravityReading.x}, y=${gravityReading.y}, z=${gravityReading.z}",
//            style = MaterialTheme.typography.bodyLarge,
//            fontSize = 20.sp
//        )
//    }
}
