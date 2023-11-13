package com.example.metronome.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val tracksRepository: TracksRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineTracksRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [TracksRepository]
     */
    override val tracksRepository: TracksRepository by lazy {
        OfflineTracksRepository(MetronomeDatabase.getDatabase(context).trackDao())
    }
}
