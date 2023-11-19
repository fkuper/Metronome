package com.fkuper.metronome.ui.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fkuper.metronome.data.Track
import com.fkuper.metronome.data.TracksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrackPickerViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    val trackPickerUiState = tracksRepository.getAllTracks()
        .map {
            TrackPickerUiState(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TrackPickerUiState()
        )

    suspend fun deleteTrack(track: Track) {
        tracksRepository.delete(track)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class TrackPickerUiState(val trackList: List<Track> = listOf())