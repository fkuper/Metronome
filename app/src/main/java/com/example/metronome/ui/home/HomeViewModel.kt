package com.example.metronome.ui.home

import androidx.lifecycle.ViewModel
import com.example.metronome.data.Track
import com.example.metronome.utils.MetronomeConfig
import com.example.metronome.utils.NoteValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {

    private val _metronomeConfig = MutableStateFlow(MetronomeConfig())
    val metronomeConfig = _metronomeConfig.asStateFlow()

    fun updateMetronomeConfig(metronomeConfig: MetronomeConfig) {
        _metronomeConfig.value = metronomeConfig
    }

    fun updateMetronomeConfig(track: Track) {
        _metronomeConfig.value = _metronomeConfig.value.copy(
            bpm = track.bpm,
            timeSignature = track.timeSignature,
            noteValue = track.noteValue ?: NoteValue.QUARTER
        )
    }

}