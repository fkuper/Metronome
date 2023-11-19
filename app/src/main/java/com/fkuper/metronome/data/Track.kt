package com.fkuper.metronome.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fkuper.metronome.utils.NoteValue
import com.fkuper.metronome.utils.TimeSignature

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val artist: String,
    val title: String,
    val bpm: Int = 120,
    val timeSignature: TimeSignature = TimeSignature.FOUR_FOUR,
    val noteValue: NoteValue? = NoteValue.QUARTER
)