package com.example.metronome.ui.tracks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metronome.R
import com.example.metronome.ui.AppViewModelProvider
import com.example.metronome.ui.components.NoteIcon
import com.example.metronome.utils.NoteValue
import kotlinx.coroutines.launch

@Composable
fun TrackPickerScreen(
    onCreateTrackButtonClicked: () -> Unit,
    onSearchTrackButtonClicked: () -> Unit,
    onTrackEditClicked: (Track) -> Unit,
    onTrackPicked: (Track) -> Unit,
    viewModel: TrackPickerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val trackerPickerUiState by viewModel.trackPickerUiState.collectAsState()
    val viewModelScope = rememberCoroutineScope()

    TrackPickerBody(
        tracks = trackerPickerUiState.trackList,
        onCreateTrackButtonClicked = onCreateTrackButtonClicked,
        onSearchTrackButtonClicked = onSearchTrackButtonClicked,
        onTrackEditClicked = { onTrackEditClicked(it) },
        onTrackDeleteClicked = {
            viewModelScope.launch {
                viewModel.deleteTrack(it)
            }
        },
        onTrackPicked = onTrackPicked
    )
}

@Composable
private fun TrackPickerBody(
    tracks: List<Track>,
    onCreateTrackButtonClicked: () -> Unit,
    onSearchTrackButtonClicked: () -> Unit,
    onTrackEditClicked: (Track) -> Unit,
    onTrackDeleteClicked: (Track) -> Unit,
    onTrackPicked: (Track) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (tracks.isEmpty()) {
            // TODO: add a better view here when if list is empty...
            Text("Oops! Looks like you don't have any tracks yet.")
        } else {
            LazyColumn(
                modifier = Modifier.matchParentSize()
            ) {
                items(tracks) { track ->
                    TrackRow(
                        track = track,
                        onTrackEditClicked = { onTrackEditClicked(it) },
                        onTrackDeleteClicked = { onTrackDeleteClicked(it) },
                        onTrackPicked = { onTrackPicked(it) }
                    )
                }
            }
        }
        ButtonBar(onSearchTrackButtonClicked, onCreateTrackButtonClicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackRow(
    track: Track,
    onTrackPicked: (Track) -> Unit,
    onTrackEditClicked: (Track) -> Unit,
    onTrackDeleteClicked: (Track) -> Unit,
) {
    var openDropDownMenu by remember { mutableStateOf(false) }

    Card(
        onClick = { onTrackPicked(track) },
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrackTitleAndArtistColumn(track = track)
            Spacer(modifier = Modifier.weight(1.0f))
            TrackMetronomeConfigCard(track = track)
            IconButton(onClick = { openDropDownMenu = true }) {
                Icon(imageVector = Icons.Rounded.MoreHoriz, contentDescription = null)

                DropdownMenu(
                    expanded = openDropDownMenu,
                    onDismissRequest = { openDropDownMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit track") },
                        onClick = {
                            onTrackEditClicked(track)
                            openDropDownMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete track", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            onTrackDeleteClicked(track)
                            openDropDownMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TrackTitleAndArtistColumn(track: Track) {
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
}

@Composable
private fun TrackMetronomeConfigCard(track: Track) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            NoteIcon(
                noteValue = track.noteValue ?: NoteValue.QUARTER,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
            Divider(modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(
                    text = "${track.bpm}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${track.timeSignature.upper}/${track.timeSignature.lower}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ButtonBar(
    onSearchTrackButtonClicked: () -> Unit,
    onCreateTrackButtonClicked: () -> Unit
) {
    Row {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(dimensionResource(id = R.dimen.padding_small))
                .background(
                    color = MaterialTheme.colorScheme.surfaceTint,
                    shape = CircleShape
                        .copy(CornerSize(dimensionResource(id = R.dimen.corner_size_large)))
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSearchTrackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_track_button),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
            Divider(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxHeight(fraction = 0.8f)
                    .width(1.dp)
            )
            IconButton(onClick = onCreateTrackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.create_new_track_button),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackPickerScreenPreview() {
    TrackPickerBody(
        tracks = testTracks,
        onCreateTrackButtonClicked = {},
        onSearchTrackButtonClicked = {},
        onTrackPicked = {},
        onTrackDeleteClicked = {},
        onTrackEditClicked = {}
    )
}

val testTracks: List<Track> = listOf(
    Track(artist = "Rush", title = "YYZ"),
    Track(artist = "Interpol", title = "PDA"),
    Track(artist = "Joan Jett", title = "Bad Reputation"),
    Track(artist = "TJ Mack", title = "Chicas"),
    Track(artist = "The Who", title = "Pinball Wizard"),
    Track(artist = "Steely Dan", title = "Aja"),
    Track(artist = "Rush", title = "YYZ"),
    Track(artist = "Interpol", title = "PDA"),
    Track(artist = "Joan Jett", title = "Bad Reputation"),
    Track(artist = "TJ Mack", title = "Chicas"),
    Track(artist = "The Who", title = "Pinball Wizard"),
    Track(artist = "Steely Dan", title = "Aja"),
    Track(artist = "Rush", title = "YYZ"),
    Track(artist = "Interpol", title = "PDA"),
    Track(artist = "Joan Jett", title = "Bad Reputation"),
    Track(artist = "TJ Mack", title = "Chicas"),
    Track(artist = "The Who", title = "Pinball Wizard"),
    Track(artist = "Steely Dan", title = "Aja"),
)