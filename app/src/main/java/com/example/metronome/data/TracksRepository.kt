package com.example.metronome.data

import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun insert(track: Track)

    suspend fun update(track: Track)

    suspend fun delete(track: Track)

    fun getAllTracks(): Flow<List<Track>>

}