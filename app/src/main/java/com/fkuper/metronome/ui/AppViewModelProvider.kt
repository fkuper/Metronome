package com.fkuper.metronome.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fkuper.metronome.MainViewModel
import com.fkuper.metronome.MetronomeApplication
import com.fkuper.metronome.ui.home.HomeViewModel
import com.fkuper.metronome.ui.tracks.TrackCreatorViewModel
import com.fkuper.metronome.ui.tracks.TrackEditorViewModel
import com.fkuper.metronome.ui.tracks.TrackPickerViewModel
import com.fkuper.metronome.ui.tracks.TrackSearcherViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Metronome app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(metronomeApplication())
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
        initializer {
            TrackSearcherViewModel(metronomeApplication().container.tracksRepository)
        }
        initializer {
            SettingsViewModel(metronomeApplication())
        }
        initializer {
            MainViewModel(metronomeApplication().container.preferencesRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MetronomeApplication].
 */
fun CreationExtras.metronomeApplication(): MetronomeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MetronomeApplication)
