package com.fkuper.metronome.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TracksRepositoryImpl(private val trackDao: TrackDao) : TracksRepository {

    private var apiAccessToken: MySpotifyWebApiAccessToken? = null

    override suspend fun insert(track: Track) = trackDao.insert(track)

    override suspend fun update(track: Track) = trackDao.update(track)

    override suspend fun delete(track: Track) = trackDao.delete(track)

    override fun getTrack(id: Int): Flow<Track?> = trackDao.getTrack(id)

    override fun getAllTracks(): Flow<List<Track>> = trackDao.getAllTracks()

    override suspend fun searchForTrack(title: String): Flow<List<Track>> {
        if (apiAccessToken == null || apiAccessToken?.isValid == false) {
            apiAccessToken = SpotifyApiClient.auth.getAccessToken().toMyImpl()
        }

        // TODO: request tracks here
        return flowOf()
    }

}