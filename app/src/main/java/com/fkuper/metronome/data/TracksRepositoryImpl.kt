package com.fkuper.metronome.data

import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(private val trackDao: TrackDao) : TracksRepository {

    override suspend fun insert(track: Track) = trackDao.insert(track)

    override suspend fun update(track: Track) = trackDao.update(track)

    override suspend fun delete(track: Track) = trackDao.delete(track)

    override fun getTrack(id: Int): Flow<Track?> = trackDao.getTrack(id)

    override fun getAllTracks(): Flow<List<Track>> = trackDao.getAllTracks()

    override fun searchForTrack(title: String): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

}