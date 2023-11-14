package com.example.metronome.ui.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metronome.data.Track
import com.example.metronome.data.TracksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrackPickerViewModel(tracksRepository: TracksRepository) : ViewModel() {

    val trackPickerUiState = tracksRepository.getAllTracks()
        .map {
            TrackPickerUiState(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TrackPickerUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class TrackPickerUiState(val trackList: List<Track> = listOf())