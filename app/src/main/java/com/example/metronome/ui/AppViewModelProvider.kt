package com.example.metronome.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.metronome.MetronomeApplication
import com.example.metronome.ui.tracks.TrackPickerScreenViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Metronome app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel()
        }
        initializer {
            TrackPickerScreenViewModel(metronomeApplication().container.tracksRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MetronomeApplication].
 */
fun CreationExtras.metronomeApplication(): MetronomeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MetronomeApplication)
