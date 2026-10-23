package com.example.lab10_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lab10_2.ui.BooksApp
import com.example.lab10_2.ui.theme.Lab10_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab10_2Theme {
                BooksApp()
            }
        }
    }
}