package com.example.agua2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.agua2.navigation.AppNavigation
import com.example.agua2.ui.theme.Agua2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Agua2Theme {
                AppNavigation()
            }
        }
    }
}
