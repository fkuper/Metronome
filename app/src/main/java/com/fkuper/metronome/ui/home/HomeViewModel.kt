package com.fkuper.metronome.ui.home

import android.content.Intent
import com.fkuper.metronome.data.Track
import com.fkuper.metronome.utils.model.MetronomeConfig
import com.fkuper.metronome.utils.model.NoteValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.fkuper.metronome.MetronomeApplication
import com.fkuper.metronome.service.MetronomeService
import com.fkuper.metronome.service.TickListener
import com.fkuper.metronome.utils.ext.putParcelableExtra

class HomeViewModel(
    private val application: MetronomeApplication
): AndroidViewModel(application), TickListener {

    private val prefsRepo = application.container.preferencesRepository

    private val _metronomeConfig = MutableStateFlow(
        prefsRepo.getMetronomeConfig() ?: MetronomeConfig()
    )
    val metronomeConfig = _metronomeConfig.asStateFlow()

    private val _metronomeIsPlaying = MutableStateFlow(false)
    val metronomeIsPlaying = _metronomeIsPlaying.asStateFlow()

    private val _metronomeTickCounter = MutableStateFlow(0)
    val metronomeTickCounter = _metronomeTickCounter.asStateFlow()

    fun updateMetronomeConfig(metronomeConfig: MetronomeConfig) {
        _metronomeConfig.value = metronomeConfig
        prefsRepo.setMetronomeConfig(metronomeConfig)
        if (_metronomeIsPlaying.value) {
            startMetronomeService()
        }
    }

    fun updateMetronomeConfig(track: Track) {
        _metronomeConfig.value = _metronomeConfig.value.copy(
            bpm = track.bpm,
            timeSignature = track.timeSignature,
            noteValue = track.noteValue ?: NoteValue.QUARTER
        )
        prefsRepo.setMetronomeConfig(metronomeConfig.value)
        if (_metronomeIsPlaying.value) {
            startMetronomeService()
        }
    }

    fun startMetronomeService() {
        application.metronomeService.setListener(this)

        Intent(application.applicationContext, MetronomeService::class.java).also {
            it.action = MetronomeService.Action.START.name
            it.putExtra(MetronomeService.Extra.BPM.name, _metronomeConfig.value.bpm)
            it.putParcelableExtra(MetronomeService.Extra.TIME_SIGNATURE.name, _metronomeConfig.value.timeSignature)
            it.putParcelableExtra(MetronomeService.Extra.NOTE_VALUE.name, _metronomeConfig.value.noteValue)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                application.startForegroundService(it)
            } else {
                application.startService(it)
            }
        }
    }

    fun stopMetronomeService() {
        Intent(application.applicationContext, MetronomeService::class.java).also {
            it.action = MetronomeService.Action.STOP.name

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                application.startForegroundService(it)
            } else {
                application.startService(it)
            }
        }
    }

    override fun onStartTicks() {
        _metronomeIsPlaying.value = true
    }

    override fun onTick(tickCount: Int) {
        _metronomeTickCounter.value = tickCount
    }

    override fun onStopTicks() {
        _metronomeTickCounter.value = 0
        _metronomeIsPlaying.value = false
    }

}