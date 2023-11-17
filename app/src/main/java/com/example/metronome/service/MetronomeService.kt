package com.example.metronome.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.metronome.R
import com.example.metronome.utils.MetronomeConfig
import com.example.metronome.utils.NoteValue
import com.example.metronome.utils.TimeSignature

class MetronomeService : Service(), TickListener {

    private val binder = LocalBinder()
    private lateinit var metronomeEngine: MetronomeEngine
    private var listener: TickListener? = null

    override fun onCreate() {
        super.onCreate()
        metronomeEngine = MetronomeEngine(this)
        metronomeEngine.setListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if ((intent != null) && (intent.action != null)) {
            when (intent.action) {
                Action.START.name -> {
                    pause()
                    play(getMetronomeConfigFrom(intent))
                }
                Action.STOP.name -> pause()
            }
        }

        return START_STICKY
    }

    fun setListener(listener: TickListener) {
        this.listener = listener
    }

    private fun play(config: MetronomeConfig) {
        metronomeEngine.start(config)

        val notification = NotificationCompat
            .Builder(this, "metronome_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)
    }

    private fun pause() {
        metronomeEngine.reset()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun getMetronomeConfigFrom(intent: Intent): MetronomeConfig {
        val config = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            MetronomeConfig(
                bpm = intent.getIntExtra(Extra.BPM.name, 120),
                timeSignature = intent.getParcelableExtra(Extra.TIME_SIGNATURE.name, TimeSignature::class.java) ?: TimeSignature.FOUR_FOUR,
                noteValue = intent.getParcelableExtra(Extra.NOTE_VALUE.name, NoteValue::class.java) ?: NoteValue.QUARTER
            )
        } else {
            MetronomeConfig(
                bpm = intent.getIntExtra(Extra.BPM.name, 120),
                timeSignature = intent.getParcelableExtra(Extra.TIME_SIGNATURE.name) ?: TimeSignature.FOUR_FOUR,
                noteValue = intent.getParcelableExtra(Extra.NOTE_VALUE.name) ?: NoteValue.QUARTER
            )
        }

        return config
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        listener = null
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {
        fun getService(): MetronomeService = this@MetronomeService
    }

    override fun onStartTicks(tickCount: Int) { listener?.onStartTicks(tickCount) }
    override fun onTick(tickCount: Int) { listener?.onTick(tickCount) }
    override fun onStopTicks() { listener?.onStopTicks() }

    enum class Action {
        START, STOP
    }

    enum class Extra {
        BPM, TIME_SIGNATURE, NOTE_VALUE
    }

}