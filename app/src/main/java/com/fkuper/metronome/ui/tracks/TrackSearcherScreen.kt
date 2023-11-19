package com.fkuper.metronome.ui.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun TrackSearcherScreen(
    viewModel: TrackSearcherViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val viewModelScope = rememberCoroutineScope()
    val tracks = viewModel.spotifyTracks.observeAsState()

    TrackSearcherScreenBody(
        tracks = tracks.value,
        onSearchForTracks = {
            viewModelScope.launch {
                viewModel.searchForTrackByTitle(it)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun TrackSearcherScreenBody(
    tracks: List<SpotifyTrack>?,
    onSearchForTracks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Button(onClick = { onSearchForTracks("test") }) {
            Text("Search for tracks")
        }
        if (tracks != null) {
            LazyColumn {
                items(tracks) { track ->
                    SpotifyTrackRow(track = track)
                }
            }
        }
    }
}

@Composable
private fun SpotifyTrackRow(track: SpotifyTrack) {
    Text(track.title)
}

@Preview(showBackground = true)
@Composable
private fun TrackSearcherScreenBodyPreview() {
    TrackSearcherScreenBody(
        tracks = null,
        modifier = Modifier.fillMaxSize(),
        onSearchForTracks = {}
    )
}