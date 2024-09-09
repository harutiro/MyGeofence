package net.harutiro.mygeofence

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.harutiro.mygeofence.ui.home.Home
import net.harutiro.mygeofence.ui.theme.MyGeofenceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGeofenceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Home(innerPadding)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyGeofenceTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Home(innerPadding)
        }
    }
}