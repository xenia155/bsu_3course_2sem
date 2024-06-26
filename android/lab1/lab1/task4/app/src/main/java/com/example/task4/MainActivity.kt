package com.example.task4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(this)
        }
    }
}

@Composable
fun MainScreen(activity: ComponentActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val photo = painterResource(id = R.drawable.photo)
        Image(
            painter = photo,
            contentDescription = "Photo",
            modifier = Modifier
                .aspectRatio(2.06f)
                .padding(top = 16.dp)
        )
        Text(
            text = "tg: xxxenixx",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        Text(
            text = "hobbies: programming, sport, drawing",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp)
        )

        Button(
            onClick = { openWebsite(activity) },
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Text(text = "Subscribe")
        }
    }
}

private fun openWebsite(activity: ComponentActivity) {
    val url = "https://t.me/xxxenixx"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(intent)
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(ComponentActivity())
}
