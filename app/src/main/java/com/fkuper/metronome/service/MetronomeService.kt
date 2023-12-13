package com.fkuper.metronome.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.fkuper.metronome.MetronomeApplication
import com.fkuper.metronome.R
import com.fkuper.metronome.utils.model.MetronomeConfig
import com.fkuper.metronome.utils.model.NoteValue
import com.fkuper.metronome.utils.model.TimeSignature

class MetronomeService : LifecycleService(), TickListener {

    private val binder = LocalBinder()
    private lateinit var metronomeEngine: MetronomeEngine
    private var listener: TickListener? = null

    override fun onCreate() {
        super.onCreate()
        metronomeEngine = MetronomeEngine(this, lifecycle)
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

        return super.onStartCommand(intent, flags, startId)
    }

    fun setListener(listener: TickListener) {
        this.listener = listener
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun play(config: MetronomeConfig) {
        metronomeEngine.start(config)

        val intent = Intent(this, MetronomeService::class.java)
        intent.action = Action.STOP.name

        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat
            .Builder(this, MetronomeApplication.NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(getString(R.string.metronome_running_notification_title))
            .setContentText(getString(R.string.metronome_running_notification_text))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(MetronomeApplication.METRONOME_RUNNING_NOTIFICATION_ID, notification)
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
        super.onBind(intent)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        listener = null
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {
        fun getService(): MetronomeService = this@MetronomeService
    }

    override fun onStartTicks() { listener?.onStartTicks() }
    override fun onTick(tickCount: Int) { listener?.onTick(tickCount) }
    override fun onStopTicks() { listener?.onStopTicks() }

    enum class Action {
        START, STOP
    }

    enum class Extra {
        BPM, TIME_SIGNATURE, NOTE_VALUE
    }

}