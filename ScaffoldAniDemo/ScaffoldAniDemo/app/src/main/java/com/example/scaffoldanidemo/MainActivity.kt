package com.example.scaffoldanidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import com.example.scaffoldanidemo.ui.theme.ScaffoldAniDemoTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.System.exit
import kotlin.math.cos
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldAniDemoTheme {
                val windowSize = calculateWindowSizeClass(this)
                AdaptiveLayout(windowSize)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveLayout(winSize: WindowSizeClass) {
    when (winSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Scaffold (
                topBar = {
                    TopAppBar(title = {Text("Top Bar Items")},
                              colors= TopAppBarDefaults.topAppBarColors(
                                   containerColor = MaterialTheme.colorScheme.primaryContainer,
                                   titleContentColor = MaterialTheme.colorScheme.primary
                              )
                    )
                },
                bottomBar = {
                    BottomAppBar {
                        Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center){
                            Text("Bottom Bar Items")
                        }
                    }
                },

                floatingActionButton = {
                    FloatingActionButton(onClick = {},
                                         containerColor = Color.LightGray)
                    {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                })


            //Main Content
            { innerPadding ->
                Column(Modifier.padding(innerPadding).padding(5.dp)) {
                    var visible by remember { mutableStateOf(true) }

                    Text(
                        "This is the main content",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(onClick = { visible = !visible }) { Text("Animate") }

                    Spacer(Modifier.height(20.dp))
                    // AnimatedVisibility
                    AnimatedVisibility(
                        visible,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Animation",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    // Another animation tool: Animate*AsState
                    var expanded by remember { mutableStateOf(false) }
                    val size by animateDpAsState(
                        targetValue = if (expanded) 200.dp else 50.dp,
                        label = "Box size"
                    )
                    Spacer(Modifier.height(50.dp))
                    Box(modifier = Modifier
                                   .size(size)
                                   .background(MaterialTheme.colorScheme.primary)
                                   //.align (Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(onClick = { expanded = !expanded }) {
                        Text("AnimateAsState")
                    }

                }
            }


        }
        WindowWidthSizeClass.Expanded -> {
            Row(Modifier.fillMaxWidth().padding(50.dp), horizontalArrangement=Arrangement.Center) {
                Text("This is an expanded screen", fontSize = 50.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}