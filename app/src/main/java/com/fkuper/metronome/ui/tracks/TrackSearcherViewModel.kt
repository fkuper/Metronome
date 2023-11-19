package com.fkuper.metronome.ui.tracks

import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.Track
import com.fkuper.metronome.data.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TrackSearcherViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    var tracks: Flow<List<Track>> = MutableStateFlow(listOf())

    suspend fun searchForTrackByTitle(title: String) {
        tracks = tracksRepository.searchForTrack(title)
    }

}