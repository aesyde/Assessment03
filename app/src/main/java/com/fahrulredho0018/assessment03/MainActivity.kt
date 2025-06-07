package com.fahrulredho0018.assessment03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fahrulredho0018.assessment03.ui.theme.Assessment03Theme
import com.fahrulredho0018.assessment03.ui.theme.screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assessment03Theme {
                MainScreen()
            }
        }
    }
}