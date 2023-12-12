package com.fkuper.metronome.ui.tracks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fkuper.metronome.data.TracksRepository
import com.fkuper.metronome.utils.model.MetronomeScreen
import com.fkuper.metronome.utils.ext.TrackDetails
import com.fkuper.metronome.utils.ext.TrackDetails.Companion.validateInput
import com.fkuper.metronome.utils.ext.TrackUiState
import com.fkuper.metronome.utils.ext.toTrack
import com.fkuper.metronome.utils.ext.toTrackUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TrackEditorViewModel(
    savedStateHandle: SavedStateHandle,
    private val tracksRepository: TracksRepository
) : ViewModel() {

    var trackUiState by mutableStateOf(TrackUiState())
        private set

    private val trackId: Int = checkNotNull(savedStateHandle[MetronomeScreen.TrackEditor.navArgumentName!!])

    init {
        viewModelScope.launch {
            trackUiState = tracksRepository.getTrack(trackId)
                .filterNotNull()
                .first()
                .toTrackUiState(true)
        }
    }

    fun updateUiState(trackDetails: TrackDetails) {
        trackUiState = TrackUiState(
            trackDetails = trackDetails,
            isEntryValid = validateInput(trackDetails)
        )
    }

    suspend fun updateTrack() {
        if (validateInput(trackUiState.trackDetails)) {
            tracksRepository.update(trackUiState.trackDetails.toTrack())
        }
    }

}