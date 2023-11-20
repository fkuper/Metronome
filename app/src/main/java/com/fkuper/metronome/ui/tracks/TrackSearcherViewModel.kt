package com.fkuper.metronome.ui.tracks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.data.TracksRepository
import com.fkuper.metronome.data.toMetronomeTrack

class TrackSearcherViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    val spotifyTracks: MutableLiveData<List<SpotifyTrack>> by lazy {
        MutableLiveData<List<SpotifyTrack>>()
    }

    suspend fun searchForTrackByTitle(title: String) {
        spotifyTracks.value = tracksRepository.searchForTrack(title)
    }

    suspend fun addTrackToPlaylist(spotifyTrack: SpotifyTrack) {
        val audioFeatures = tracksRepository.getSpotifyTracksAudioFeatures(spotifyTrack.spotifyId)
        val metronomeTrack = audioFeatures.toMetronomeTrack(spotifyTrack)
        tracksRepository.insert(metronomeTrack)
    }

    suspend fun removeTrackFromPlaylist(track: SpotifyTrack) {
        // TODO: implement me
    }

}