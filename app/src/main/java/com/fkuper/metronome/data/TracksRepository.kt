package com.fkuper.metronome.data

import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun insert(track: Track)

    suspend fun update(track: Track)

    suspend fun delete(track: Track)

    suspend fun delete(spotifyId: String)

    fun getTrack(id: Int): Flow<Track?>

    suspend fun getTrack(spotifyId: String): Track?

    fun getAllTracks(): Flow<List<Track>>

    suspend fun searchForTrack(title: String): Result<SpotifySearchResult>

    suspend fun getSpotifyTracksAudioFeatures(id: String): Result<SpotifyTrackAudioFeatures>

}