package com.example.metronome.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.metronome.MetronomeApplication
import com.example.metronome.ui.home.HomeViewModel
import com.example.metronome.ui.tracks.TrackCreatorViewModel
import com.example.metronome.ui.tracks.TrackEditorViewModel
import com.example.metronome.ui.tracks.TrackPickerViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Metronome app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel()
        }
        initializer {
            TrackPickerViewModel(metronomeApplication().container.tracksRepository)
        }
        initializer {
            TrackCreatorViewModel(metronomeApplication().container.tracksRepository)
        }
        initializer {
            TrackEditorViewModel(
                this.createSavedStateHandle(),
                metronomeApplication().container.tracksRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MetronomeApplication].
 */
fun CreationExtras.metronomeApplication(): MetronomeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MetronomeApplication)
