package com.example.metronome.utils

import com.example.metronome.data.Track

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
) {
    companion object {
        fun validateInput(trackDetails: TrackDetails): Boolean {
            return with(trackDetails) {
                artist.isNotBlank() && title.isNotBlank()
            }
        }
    }
}

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