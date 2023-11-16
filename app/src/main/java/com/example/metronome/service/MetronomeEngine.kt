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
    private var soundId: Int = -1
    private var delayIntervalInMilliSeconds: Long = 500

    private val thread = HandlerThread("metronome_engine")

    init {
        val audioAttributes = AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        soundPool = SoundPool
            .Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundId = soundPool.load(context, R.raw.metronome_click_1, 1)

        thread.start()
        handler = Handler(thread.looper)
    }

    fun start(config: MetronomeConfig) {
        metronomeConfig = config
        updateDelayInterval()
        isRunning = true
        listener?.onStartTicks()
        handler.post(this::run)
    }

    fun pause() {
        handler.removeCallbacks(thread)
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

    private fun run() {
        if (isRunning) {
            handler.postDelayed(this::run, delayIntervalInMilliSeconds)

            listener?.onTick()
            if (soundId != -1) {
                soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
            }
        }
    }

}