package com.fkuper.metronome.data

import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(private val trackDao: TrackDao) : TracksRepository {

    private var apiAccessToken: MySpotifyWebApiAccessToken? = null

    override suspend fun insert(track: Track) = trackDao.insert(track)
    override suspend fun update(track: Track) = trackDao.update(track)
    override suspend fun delete(track: Track) = trackDao.delete(track)
    override suspend fun delete(spotifyId: String) = trackDao.delete(spotifyId)
    override fun getTrack(id: Int): Flow<Track?> = trackDao.getTrack(id)
    override suspend fun getTrack(spotifyId: String): Track? = trackDao.getTrack(spotifyId)
    override fun getAllTracks(): Flow<List<Track>> = trackDao.getAllTracks()

    override suspend fun searchForTrack(title: String): Result<SpotifySearchResult> {
        validateAccessToken()
        return SpotifyApiClient.api.search(auth = authString, query = title)
    }

    override suspend fun getSpotifyTracksAudioFeatures(id: String): Result<SpotifyTrackAudioFeatures> {
        validateAccessToken()
        return SpotifyApiClient.api.getTracksAudioFeatures(auth = authString, tracksSpotifyId = id)
    }

    private suspend fun validateAccessToken() {
        if (apiAccessToken == null || apiAccessToken?.isValid == false) {
            apiAccessToken = SpotifyApiClient.auth.getAccessToken().toMyImpl()
        }
    }

    private val authString: String get() {
        return "Bearer ${apiAccessToken?.token}"
    }

}