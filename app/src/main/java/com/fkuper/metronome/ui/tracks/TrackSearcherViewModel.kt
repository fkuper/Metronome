package com.fkuper.metronome.ui.tracks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.data.TracksRepository

class TrackSearcherViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    val spotifyTracks: MutableLiveData<List<SpotifyTrack>> by lazy {
        MutableLiveData<List<SpotifyTrack>>()
    }

    suspend fun searchForTrackByTitle(title: String) {
        spotifyTracks.value = tracksRepository.searchForTrack(title)
    }

}