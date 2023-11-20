package com.fkuper.metronome.ui.tracks

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.data.TracksRepository
import com.fkuper.metronome.data.toMetronomeTrack

class TrackSearcherViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    val tracksMap: SnapshotStateMap<String, SpotifyTrackUiState> = mutableStateMapOf()

    suspend fun searchForTrackByTitle(title: String) {
        val searchResultMap = tracksRepository.searchForTrack(title)
            .associateBy({ it.id }, { SpotifyTrackUiState(it) })

        tracksMap.putAll(searchResultMap)
        tracksMap.forEach {
            tracksMap[it.key] = it.value.copy(isInPlaylist = isInPlaylist(it.value.spotifyTrack))
        }
    }

    suspend fun addTrackToPlaylist(spotifyTrack: SpotifyTrack) {
        val audioFeatures = tracksRepository.getSpotifyTracksAudioFeatures(spotifyTrack.id)
        val metronomeTrack = audioFeatures.toMetronomeTrack(spotifyTrack)
        tracksRepository.insert(metronomeTrack)

        val updatedValue = tracksMap[spotifyTrack.id]?.copy(isInPlaylist = true)
        if (updatedValue != null) tracksMap[spotifyTrack.id] = updatedValue
    }

    suspend fun removeTrackFromPlaylist(spotifyTrack: SpotifyTrack) {
        tracksRepository.delete(spotifyTrack.id)

        val updatedValue = tracksMap[spotifyTrack.id]?.copy(isInPlaylist = false)
        if (updatedValue != null) tracksMap[spotifyTrack.id] = updatedValue
    }

    private suspend fun isInPlaylist(spotifyTrack: SpotifyTrack): Boolean {
        return tracksRepository.getTrack(spotifyTrack.id) != null
    }

}

data class SpotifyTrackUiState(
    val spotifyTrack: SpotifyTrack,
    val isInPlaylist: Boolean = false
)