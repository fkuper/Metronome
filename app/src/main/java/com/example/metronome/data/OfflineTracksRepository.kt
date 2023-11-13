package com.example.metronome.data

import kotlinx.coroutines.flow.Flow

class OfflineTracksRepository(private val trackDao: TrackDao) : TracksRepository {

    override suspend fun insert(track: Track) = trackDao.insert(track)

    override suspend fun update(track: Track) = trackDao.update(track)

    override suspend fun delete(track: Track) = trackDao.delete(track)

    override fun getAllTracks(): Flow<List<Track>> = trackDao.getAllTracks()

}