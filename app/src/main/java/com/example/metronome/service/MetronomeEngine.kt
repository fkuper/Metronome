package com.example.metronome.service

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Handler
import android.os.HandlerThread
import com.example.metronome.R
import com.example.metronome.utils.MetronomeConfig

class MetronomeEngine(context: Context) {

    private var isRunning: Boolean = false
    private var listener: TickListener? = null

    private var handler: Handler
    private var soundPool: SoundPool

    private var metronomeConfig = MetronomeConfig()
    private var soundIdNormal: Int = -1
    private var soundIdAccent: Int = -1
    private var delayIntervalInMilliSeconds: Long = 500
    private var tickCounter: Int = -1

    private val thread = HandlerThread("metronome_engine")

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

        thread.start()
        handler = Handler(thread.looper)
    }

    fun start(config: MetronomeConfig) {
        metronomeConfig = config
        updateDelayInterval()
        isRunning = true

        listener?.onStartTicks(tickCounter)
        handler.post(this::run)
    }

    fun reset() {
        handler.removeCallbacks(thread)
        tickCounter = -1
        listener?.onStopTicks()
        isRunning = false
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

    private fun advanceByOneTick() {
        tickCounter = (tickCounter + 1) % metronomeConfig.timeSignature.upper
        listener?.onTick(tickCounter)
    }

    private fun playTickSound() {
        if (tickCounter == 0) {
            if (soundIdAccent != -1) {
                soundPool.play(soundIdAccent, 1F, 1F, 0, 0, 1F)
            }
        } else {
            if (soundIdNormal != -1) {
                soundPool.play(soundIdNormal, 1F, 1F, 0, 0, 1F)
            }
        }
    }

    private fun run() {
        if (isRunning) {
            handler.postDelayed(this::run, delayIntervalInMilliSeconds)
            advanceByOneTick()
            playTickSound()
        }
    }

}