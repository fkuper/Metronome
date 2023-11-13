package com.example.metronome

import android.app.Application
import com.example.metronome.data.AppContainer
import com.example.metronome.data.AppDataContainer

class MetronomeApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}