package com.fkuper.metronome.ui.tracks

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.data.TracksRepository
import com.fkuper.metronome.data.toMetronomeTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrackSearcherViewModel(private val tracksRepository: TracksRepository) : ViewModel() {

    val searchState = MutableStateFlow<SearchState>(SearchState.START)

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    private val _tracksState = MutableStateFlow<SnapshotStateMap<String, SpotifyTrackUiState>>(
        mutableStateMapOf()
    )
    val tracksState: StateFlow<Map<String, SpotifyTrackUiState>> = _tracksState

    fun updateSearchString(value: String) {
        _searchString.value = value
    }

    suspend fun searchForTracks() {
        searchState.value = SearchState.LOADING

        tracksRepository.searchForTrack(_searchString.value)
            .onSuccess { searchResult ->
                val searchResultMap = searchResult.tracks.items
                    .associateBy({ it.id }, { SpotifyTrackUiState(it) })

                _tracksState.value.putAll(searchResultMap)
                _tracksState.value.forEach {
                    _tracksState.value[it.key] =
                        it.value.copy(isInPlaylist = isInPlaylist(it.value.spotifyTrack))
                }
                searchState.value = SearchState.SUCCESS
            }
            .onFailure {
                searchState.emit(SearchState.FAILURE(it.localizedMessage))
            }
    }

    suspend fun addTrackToPlaylist(spotifyTrack: SpotifyTrack) {
        tracksRepository.getSpotifyTracksAudioFeatures(spotifyTrack.id)
            .onSuccess { audioFeatures ->
                val metronomeTrack = audioFeatures.toMetronomeTrack(spotifyTrack)
                tracksRepository.insert(metronomeTrack)
                updateTrackPlaylistState(SpotifyTrackUiState(
                    spotifyTrack, isInPlaylist = true, justAdded = true
                ))
            }
            .onFailure {
                updateTrackPlaylistState(
                    SpotifyTrackUiState(
                        spotifyTrack = spotifyTrack,
                        isInPlaylist = false,
                        failureMessage = "Something went wrong: ${it.localizedMessage}"
                    )
                )
            }
    }

    suspend fun removeTrackFromPlaylist(spotifyTrack: SpotifyTrack) {
        tracksRepository.delete(spotifyTrack.id)
        updateTrackPlaylistState(SpotifyTrackUiState(
            spotifyTrack, isInPlaylist = false, justRemoved = true
        ))
    }

    fun failureMessageShown(state: SpotifyTrackUiState) {
        updateTrackPlaylistState(state.copy(failureMessage = null))
    }

    fun successMessageShown(state: SpotifyTrackUiState) {
        updateTrackPlaylistState(state.copy(justAdded = false))
    }

    fun removeMessageShown(state: SpotifyTrackUiState) {
        updateTrackPlaylistState(state.copy(justRemoved = false))
    }

    private suspend fun isInPlaylist(spotifyTrack: SpotifyTrack): Boolean {
        return tracksRepository.getTrack(spotifyTrack.id) != null
    }

    private fun updateTrackPlaylistState(state: SpotifyTrackUiState) {
        val updatedValue = _tracksState.value[state.spotifyTrack.id]?.copy(
            spotifyTrack = state.spotifyTrack,
            isInPlaylist = state.isInPlaylist,
            failureMessage = state.failureMessage,
            justAdded = state.justAdded,
            justRemoved = state.justRemoved
        )
        if (updatedValue != null) {
            _tracksState.value[state.spotifyTrack.id] = updatedValue
        }
    }

}

data class SpotifyTrackUiState(
    val spotifyTrack: SpotifyTrack,
    val isInPlaylist: Boolean = false,
    val failureMessage: String? = null,
    val justAdded: Boolean = false,
    val justRemoved: Boolean = false
)

sealed class SearchState {
    data object START : SearchState()
    data object LOADING : SearchState()
    data object SUCCESS : SearchState()
    data class FAILURE(val message: String?) : SearchState()
}