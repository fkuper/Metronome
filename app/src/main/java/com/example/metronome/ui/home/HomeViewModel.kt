package com.example.metronome.ui.home

import android.content.Intent
import com.example.metronome.data.Track
import com.example.metronome.utils.MetronomeConfig
import com.example.metronome.utils.NoteValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.example.metronome.MetronomeApplication
import com.example.metronome.service.TickListener

class HomeViewModel(
    private val application: MetronomeApplication
): AndroidViewModel(application), TickListener {

    private val _metronomeConfig = MutableStateFlow(MetronomeConfig())
    val metronomeConfig = _metronomeConfig.asStateFlow()

    private val _metronomePlaybackState = MutableStateFlow(false)
    val metronomeIsPlaying = _metronomePlaybackState.asStateFlow()

    private val _metronomeTickCounter = MutableStateFlow(0)
    val metronomeTickCounter = _metronomeTickCounter.asStateFlow()

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

    fun startMetronomeService(intent: Intent) {
        application.metronomeService.setListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(intent)
        } else {
            application.startService(intent)
        }
        _metronomePlaybackState.value = true
    }

    fun stopMetronomeService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(intent)
        } else {
            application.startService(intent)
        }
        _metronomePlaybackState.value = false
    }

    override fun onStartTicks(tickCount: Int) {
        _metronomeTickCounter.value = tickCount
    }

    override fun onTick(tickCount: Int) {
        _metronomeTickCounter.value = tickCount
    }

    override fun onStopTicks() {
        _metronomeTickCounter.value = 0
    }

}