package com.example.metronome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.metronome.ui.home.MetronomeHomeScreen
import com.example.metronome.ui.theme.MetronomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MetronomeTheme {
                MetronomeHomeScreen()
            }
        }
    }
}