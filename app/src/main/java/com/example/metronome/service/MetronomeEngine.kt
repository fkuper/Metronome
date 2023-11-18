package com.example.metronome.service

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.metronome.R
import com.example.metronome.utils.MetronomeConfig
import java.util.Timer
import java.util.TimerTask

class MetronomeEngine(context: Context) {

    private var soundPool: SoundPool
    private var soundIdNormal: Int = -1
    private var soundIdAccent: Int = -1

    private var metronomeConfig = MetronomeConfig()
    private var delayIntervalInMilliSeconds: Long = 500
    private var tickCounter: Int = -1

    private var tickTimer: Timer? = null
    private var listener: TickListener? = null

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

        tickTimer = Timer("metronome_timer", true)
        tickTimer?.scheduleAtFixedRate(RunTask(), 0, delayIntervalInMilliSeconds)

        listener?.onStartTicks(tickCounter)
    }

    fun reset() {
        tickTimer?.cancel()
        tickCounter = -1
        listener?.onStopTicks()
    }

    fun setListener(listener: TickListener) {
        this.listener = listener
    }

    private fun updateDelayInterval() {
        val beatsPerSecond = metronomeConfig.bpm.toFloat() / 60F
        val delayInSeconds = 1F / beatsPerSecond
        val delayInMilliSeconds = (delayInSeconds * 1000).toLong()

        delayIntervalInMilliSeconds = delayInMilliSeconds
    }

    inner class RunTask : TimerTask() {
        override fun run() {
            advanceByOneTick()
            playTickSound()
        }

        private fun advanceByOneTick() {
            tickCounter = (tickCounter + 1) % metronomeConfig.timeSignature.upper
            listener?.onTick(tickCounter)
        }

        private fun playTickSound() {
            val soundId = if (tickCounter == 0) soundIdAccent else soundIdNormal
            soundPool.play(soundId, 1F, 1F, 1, 0, 1F)
        }
    }

}