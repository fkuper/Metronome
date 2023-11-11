package com.example.metronome.utils

data class MetronomeConfig(
    var bpm: Int = 120,
    var timeSignature: TimeSignature = TimeSignature.FOUR_FOUR,
    var noteValue: NoteValue = NoteValue.QUARTER
)