
package com.sad.lab4_task2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestureTrackingApp()
        }
    }
}

@Composable
fun GestureTrackingApp() {
    var boxColor by remember { mutableStateOf(Color.Transparent) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        boxColor = Color.Blue
                    },
                    onDoubleTap = {
                        boxColor = Color.Green
                    },
                    onLongPress = {
                        boxColor = Color.Red
                    },
                    onPress = { boxColor = Color.Gray }
                )
            }
    ) {
        Text("Single Tap: Change to Blue", Modifier.padding(16.dp))
        Text("Double Tap: Change to Green", Modifier.padding(16.dp))
        Text("Long Press: Change to Red", Modifier.padding(16.dp))
        Text("Press: Change to Gray", Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(boxColor)
        )
    }
}

