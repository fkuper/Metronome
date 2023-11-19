package com.fkuper.metronome.ui.tracks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fkuper.metronome.R
import com.fkuper.metronome.data.SpotifyTrack
import com.fkuper.metronome.ui.AppViewModelProvider
import com.fkuper.metronome.ui.components.TrackRow
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
        onAddClicked = {
            // TODO implement me
        },
        onRemoveClicked = {
            // TODO implement me
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun TrackSearcherScreenBody(
    tracks: List<SpotifyTrack>?,
    onSearchForTracks: (String) -> Unit,
    onAddClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        TrackSearchBar(onSearchForTracks = { onSearchForTracks(it) })
        
        if (tracks == null) return
        LazyColumn {
            items(tracks) { track ->
                SpotifyTrackRow(
                    track = track,
                    onAddClicked = onAddClicked,
                    onRemoveClicked = onRemoveClicked
                )
            }
        }
    }
}

@Composable
private fun SpotifyTrackRow(
    track: SpotifyTrack,
    onAddClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
) {
    var didAddSongToLibrary by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        AlbumArtView(url = track.album.images.first().url)
        TrackRow(
            title = track.title,
            artist = track.artists.first().name,
            onInteractionButtonClicked = {
                didAddSongToLibrary = if (!didAddSongToLibrary) {
                    onAddClicked(); true
                } else {
                    onRemoveClicked(); false
                }
            },
            interactionButtonIcon =
                if (!didAddSongToLibrary) {
                    Icons.Rounded.Add
                } else {
                    Icons.Rounded.Remove
                }
        )
    }
}

@Composable
private fun TrackSearchBar(
    onSearchForTracks: (String) -> Unit
) {
    // TODO: implement text field and use its value as input
    val searchString = "test"

    Row {
        Button(onClick = { onSearchForTracks(searchString) }) {
            Text("Search for tracks")
        }
    }
}

@Composable
private fun AlbumArtView(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            .clip(RoundedCornerShape(10))
            .border(
                border = BorderStroke(
                    width = dimensionResource(R.dimen.border_width_large),
                    color = MaterialTheme.colorScheme.surfaceTint
                ),
                shape = RoundedCornerShape(10)
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun TrackSearcherScreenBodyPreview() {
    TrackSearcherScreenBody(
        tracks = null,
        modifier = Modifier.fillMaxSize(),
        onAddClicked = {},
        onRemoveClicked = {},
        onSearchForTracks = {}
    )
}