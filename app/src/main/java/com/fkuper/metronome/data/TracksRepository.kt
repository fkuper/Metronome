package com.fkuper.metronome.data

import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun insert(track: Track)

    suspend fun update(track: Track)

    suspend fun delete(track: Track)

    fun getTrack(id: Int): Flow<Track?>

    fun getAllTracks(): Flow<List<Track>>

    suspend fun searchForTrack(title: String): List<SpotifyTrack>

    suspend fun getSpotifyTracksAudioFeatures(id: String): SpotifyTrackAudioFeatures

}