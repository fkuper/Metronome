package com.fkuper.metronome

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.fkuper.metronome.data.AppContainer
import com.fkuper.metronome.data.AppDataContainer
import com.fkuper.metronome.service.MetronomeService

class MetronomeApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
        private set
    lateinit var metronomeService: MetronomeService
        private set
    private var bound: Boolean = false

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "metronome_channel",
                "Metronome Notifications",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Bind to MetronomeService
        Intent(this, MetronomeService::class.java).also {
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as MetronomeService.LocalBinder
            metronomeService = localBinder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

}