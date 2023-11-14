package com.example.metronome.ui.tracks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.metronome.data.Track
import com.example.metronome.data.TracksRepository
import com.example.metronome.utils.NoteValue
import com.example.metronome.utils.TimeSignature

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
        if (validateInput()) {
            tracksRepository.insert(trackUiState.trackDetails.toTrack())
        }
    }

    private fun validateInput(trackDetails: TrackDetails = trackUiState.trackDetails): Boolean {
        return with(trackDetails) {
            artist.isNotBlank() && title.isNotBlank()
        }
    }

}

data class TrackUiState(
    val trackDetails: TrackDetails = TrackDetails(),
    val isEntryValid: Boolean = false
)

data class TrackDetails(
    val id: Int = 0,
    val artist: String = "",
    val title: String = "",
    val bpm: Int = 120,
    val timeSignature: TimeSignature = TimeSignature.FOUR_FOUR,
    val noteValue: NoteValue? = NoteValue.QUARTER
)

fun TrackDetails.toTrack(): Track = Track (
    id = id,
    artist = artist,
    title = title,
    bpm = bpm,
    timeSignature = timeSignature,
    noteValue = noteValue
)

fun Track.toTrackDetails(): TrackDetails = TrackDetails(
    id = id,
    artist = artist,
    title = title,
    bpm = bpm,
    timeSignature = timeSignature,
    noteValue = noteValue
)

fun Track.toTrackUiState(isEntryValid: Boolean = false): TrackUiState = TrackUiState(
    trackDetails = this.toTrackDetails(),
    isEntryValid = isEntryValid
)