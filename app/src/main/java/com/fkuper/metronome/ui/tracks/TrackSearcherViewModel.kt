package com.fkuper.metronome.ui.tracks

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.data.TracksRepository
import com.fkuper.metronome.data.toMetronomeTrack
import kotlinx.coroutines.flow.MutableStateFlow

class TrackSearcherViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    val tracksMap: SnapshotStateMap<String, SpotifyTrackUiState> = mutableStateMapOf()
    val searchState = MutableStateFlow<SearchState>(SearchState.START)
    val addTrackState = MutableStateFlow<AddSpotifyTrackState>(AddSpotifyTrackState.START)

    suspend fun searchForTrackByTitle(title: String) {
        searchState.value = SearchState.LOADING

        tracksRepository.searchForTrack(title)
            .onSuccess { searchResult ->
                val searchResultMap = searchResult.tracks.items
                    .associateBy({ it.id }, { SpotifyTrackUiState(it) })
                tracksMap.putAll(searchResultMap)
                tracksMap.forEach {
                    tracksMap[it.key] = it.value.copy(isInPlaylist = isInPlaylist(it.value.spotifyTrack))
                }
                searchState.value = SearchState.SUCCESS
            }
            .onFailure {
                searchState.emit(SearchState.FAILURE(it.localizedMessage))
            }
    }

    suspend fun addTrackToPlaylist(spotifyTrack: SpotifyTrack) {
        addTrackState.value = AddSpotifyTrackState.LOADING

        tracksRepository.getSpotifyTracksAudioFeatures(spotifyTrack.id)
            .onSuccess { audioFeatures ->
                val metronomeTrack = audioFeatures.toMetronomeTrack(spotifyTrack)
                tracksRepository.insert(metronomeTrack)

                val updatedValue = tracksMap[spotifyTrack.id]?.copy(isInPlaylist = true)
                if (updatedValue != null) tracksMap[spotifyTrack.id] = updatedValue
                addTrackState.value = AddSpotifyTrackState.SUCCESS
            }
            .onFailure {
                addTrackState.emit(AddSpotifyTrackState.FAILURE(it.localizedMessage))
            }
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

sealed class AddSpotifyTrackState {
    data object START : AddSpotifyTrackState()
    data object LOADING : AddSpotifyTrackState()
    data object SUCCESS : AddSpotifyTrackState()
    data class FAILURE(val message: String?) : AddSpotifyTrackState()
}

sealed class SearchState {
    data object START : SearchState()
    data object LOADING : SearchState()
    data object SUCCESS : SearchState()
    data class FAILURE(val message: String?) : SearchState()
}