package com.fkuper.metronome.ui.tracks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.TracksRepository
import com.fkuper.metronome.utils.TrackDetails
import com.fkuper.metronome.utils.TrackDetails.Companion.validateInput
import com.fkuper.metronome.utils.TrackUiState
import com.fkuper.metronome.utils.toTrack

class TrackCreatorViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    var trackUiState by mutableStateOf(TrackUiState())
        private set

    fun updateUiState(trackDetails: TrackDetails) {
        trackUiState = TrackUiState(
            trackDetails = trackDetails,
            isEntryValid = validateInput(trackDetails)
        )
    }

    suspend fun saveTrack() {
        if (validateInput(trackUiState.trackDetails)) {
            tracksRepository.insert(trackUiState.trackDetails.toTrack())
        }
    }

}