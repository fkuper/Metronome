package com.example.metronome.utils

data class Track(
    val artist: String,
    val title: String,
    val bpm: Int = 120,
    val timeSignature: TimeSignature = TimeSignature.FOUR_FOUR,
    val noteValue: NoteValue? = NoteValue.QUARTER
)