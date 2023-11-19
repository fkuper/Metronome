package com.fkuper.metronome.utils

enum class NoteValue(val delayDivisor: Int) {
    QUARTER(delayDivisor = 1),
    EIGHTH(delayDivisor = 2),
    SIXTEENTH(delayDivisor = 4),
}