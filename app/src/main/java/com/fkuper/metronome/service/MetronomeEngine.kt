package com.fkuper.metronome.service

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.fkuper.metronome.utils.MetronomeConfig
import com.fkuper.metronome.R
import java.util.Timer
import java.util.TimerTask

class MetronomeEngine(context: Context) {

    private var soundPool: SoundPool
    private var soundIdNormal: Int = -1
    private var soundIdAccent: Int = -1

    private var metronomeConfig = MetronomeConfig()
    private var delayIntervalInMilliSeconds: Long = 500

    private var tickTimer: Timer? = null
    private var listener: TickListener? = null

    private var soundTickCounter: Int = -1
    private var numberOfSoundTicks: Int = -1

    init {
        val audioAttributes = AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        soundPool = SoundPool
            .Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        soundIdNormal = soundPool.load(context, R.raw.metronome_click_1, 1)
        soundIdAccent = soundPool.load(context, R.raw.metronome_click_2, 1)
    }

    fun start(config: MetronomeConfig) {
        metronomeConfig = config
        updateDelayInterval()
        updateNumberOfSoundTicks()

        tickTimer = Timer("metronome_timer", true)
        tickTimer?.scheduleAtFixedRate(RunTask(), 0, delayIntervalInMilliSeconds)

        listener?.onStartTicks()
    }

    fun reset() {
        tickTimer?.cancel()
        soundTickCounter = -1
        listener?.onStopTicks()
    }

    fun setListener(listener: TickListener) {
        this.listener = listener
    }

    private fun updateDelayInterval() {
        val beatsPerSecond = metronomeConfig.bpm.toFloat() / 60F
        val delayInSeconds = (1F / beatsPerSecond) / metronomeConfig.noteValue.delayDivisor
        val delayInMilliSeconds = (delayInSeconds * 1000).toLong()

        delayIntervalInMilliSeconds = delayInMilliSeconds
    }

    private fun updateNumberOfSoundTicks() {
        numberOfSoundTicks = metronomeConfig.timeSignature.upper * metronomeConfig.noteValue.delayDivisor
    }

    inner class RunTask : TimerTask() {
        override fun run() {
            advanceSoundTick()
            if (shouldAdvanceOneTick) {
                listener?.onTick(soundTickCounter / metronomeConfig.noteValue.delayDivisor)
            }
            playTickSound()
        }

        private val shouldAdvanceOneTick: Boolean
            get() = (soundTickCounter % metronomeConfig.noteValue.delayDivisor) == 0

        private fun advanceSoundTick() {
            soundTickCounter = (soundTickCounter + 1) % numberOfSoundTicks
        }

        private fun playTickSound() {
            val soundId = if (soundTickCounter == 0) soundIdAccent else soundIdNormal
            soundPool.play(soundId, 1F, 1F, 1, 0, 1F)
        }
    }

}