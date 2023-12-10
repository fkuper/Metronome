package com.fkuper.metronome.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val tracksRepository: TracksRepository
    val preferencesRepository: SharedPreferencesRepository
}

/**
 * [AppContainer] implementation that provides instance of [TracksRepositoryImpl]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [TracksRepository]
     */
    override val tracksRepository: TracksRepository by lazy {
        TracksRepositoryImpl(
            MetronomeDatabase.getDatabase(context).trackDao()
        )
    }

    override val preferencesRepository: SharedPreferencesRepository by lazy {
        SharedPreferencesRepository(context)
    }

}
