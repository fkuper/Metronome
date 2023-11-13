package com.example.metronome.ui.tracks

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.metronome.data.Track
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metronome.R
import com.example.metronome.ui.AppViewModelProvider

@Composable
fun TrackPickerScreen(
    onCreateTrackButtonClicked: () -> Unit,
    onSearchTrackButtonClicked: () -> Unit,
    onTrackPicked: (Track) -> Unit,
    viewModel: TrackPickerScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val trackerPickerUiState by viewModel.trackPickerUiState.collectAsState()

    TrackPickerBody(
        tracks = trackerPickerUiState.trackList,
        onCreateTrackButtonClicked = onCreateTrackButtonClicked,
        onSearchTrackButtonClicked = onSearchTrackButtonClicked,
        onTrackPicked = onTrackPicked
    )
}

@Composable
private fun TrackPickerBody(
    tracks: List<Track>,
    onCreateTrackButtonClicked: () -> Unit,
    onSearchTrackButtonClicked: () -> Unit,
    onTrackPicked: (Track) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (tracks.isEmpty()) {
            Text("Oops! Looks like you don't have any tracks yet.")
        } else {
            LazyColumn(
                modifier = Modifier.matchParentSize()
            ) {
                items(tracks) { track ->
                    TrackRow(track) {
                        onTrackPicked(it)
                    }
                }
            }
        }
        ButtonBar(onSearchTrackButtonClicked, onCreateTrackButtonClicked)
    }
}

@Composable
private fun TrackRow(
    track: Track,
    onTrackPicked: (Track) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(IntrinsicSize.Min)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceTint,
                shape = CircleShape.copy(CornerSize(8.dp))
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    shape = CircleShape.copy(CornerSize(8.dp))
                )
                .padding(4.dp)
        ) {
            Text(
                text = "${track.bpm} BPM",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = "${track.timeSignature.upper}/${track.timeSignature.lower}",
                style = MaterialTheme.typography.titleMedium
            )
        }
        IconButton(onClick = {
            onTrackPicked(track)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.pick_track_to_play_button)
            )
        }
    }
}

@Composable
private fun ButtonBar(
    onSearchTrackButtonClicked: () -> Unit,
    onCreateTrackButtonClicked: () -> Unit
) {
    Row {
        IconButton(onClick = onSearchTrackButtonClicked) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search_track_button)
            )
        }
        IconButton(onClick = onCreateTrackButtonClicked) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.create_new_track_button)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackPickerScreenPreview() {
    TrackPickerBody(
        tracks = testTracks,
        onCreateTrackButtonClicked = {},
        onSearchTrackButtonClicked = {},
        onTrackPicked = {}
    )
}

val testTracks: List<Track> = listOf(
    Track(artist = "Rush", title = "YYZ"),
    Track(artist = "Interpol", title = "PDA"),
    Track(artist = "Joan Jett", title = "Bad Reputation"),
    Track(artist = "TJ Mack", title = "Chicas"),
    Track(artist = "The Who", title = "Pinball Wizard"),
    Track(artist = "Steely Dan", title = "Aja"),
)