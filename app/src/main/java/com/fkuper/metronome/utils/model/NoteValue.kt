package com.fkuper.metronome.utils.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class NoteValue(val delayDivisor: Int) : Parcelable {
    QUARTER(delayDivisor = 1),
    EIGHTH(delayDivisor = 2),
    SIXTEENTH(delayDivisor = 4),
}