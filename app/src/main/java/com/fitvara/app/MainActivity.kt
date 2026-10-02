package com.fitvara.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.fitvara.app.ui.login.LoginScreen
import com.fitvara.app.ui.theme.FitvaraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FitvaraTheme {
                LoginScreen(Modifier.fillMaxSize())
            }
        }
    }
}
