package com.fkuper.metronome.ui.tracks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.fkuper.metronome.data.Track
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fkuper.metronome.R
import com.fkuper.metronome.ui.AppViewModelProvider
import com.fkuper.metronome.ui.components.NoteIcon
import com.fkuper.metronome.ui.components.TrackTitleAndArtistColumn
import com.fkuper.metronome.utils.model.NoteValue
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
    if (tracks.isEmpty()) {
        EmptyPlaylistView(
            onSearchTrackButtonClicked = onSearchTrackButtonClicked,
            onCreateTrackButtonClicked = onCreateTrackButtonClicked,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(
                modifier = Modifier.matchParentSize()
            ) {
                items(tracks) { track ->
                    TrackPickerRow(
                        track = track,
                        onTrackPicked = { onTrackPicked(it) },
                        onTrackEditClicked = { onTrackEditClicked(it) },
                        onTrackDeleteClicked = { onTrackDeleteClicked(it) }
                    )
                }
            }
            ButtonBar(onSearchTrackButtonClicked, onCreateTrackButtonClicked)
        }
    }
}

@Composable
private fun EmptyPlaylistView(
    onCreateTrackButtonClicked: () -> Unit,
    onSearchTrackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.empty_playlist_text),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        EmptyPlaylistButtons(
            onSearchTrackButtonClicked = onSearchTrackButtonClicked,
            onCreateTrackButtonClicked = onCreateTrackButtonClicked,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.PlaylistRemove,
                contentDescription = stringResource(id = R.string.empty_playlist_icon),
                modifier = Modifier
                    .size(200.dp)
                    .alpha(0.3F)
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackPickerRow(
    track: Track,
    onTrackPicked: (Track) -> Unit,
    onTrackEditClicked: (Track) -> Unit,
    onTrackDeleteClicked: (Track) -> Unit
) {
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
            TrackTitleAndArtistColumn(
                title = track.title,
                artist = track.artist,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
            )
            TrackMetronomeConfigCard(track = track)
            OpenTrackRowMenuButton(
                onTrackEditClicked = { onTrackEditClicked(track) },
                onTrackDeleteClicked = { onTrackDeleteClicked(track) }
            )
        }
    }
}

@Composable
private fun TrackMetronomeConfigCard(
    track: Track,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
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
private fun EmptyPlaylistButtons(
    onSearchTrackButtonClicked: () -> Unit,
    onCreateTrackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        FilledTonalButton(
            onClick = onSearchTrackButtonClicked,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.search_track_button),
            )
            Text("Search")
        }
        FilledTonalButton(
            onClick = onCreateTrackButtonClicked,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.create_new_track_button),
            )
            Text("Create")
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

@Composable
private fun OpenTrackRowMenuButton(
    onTrackEditClicked: () -> Unit,
    onTrackDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openDropDownMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = { openDropDownMenu = true },
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Rounded.MoreHoriz, contentDescription = null)

        DropdownMenu(
            expanded = openDropDownMenu,
            onDismissRequest = { openDropDownMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Edit track") },
                onClick = {
                    onTrackEditClicked()
                    openDropDownMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Delete track", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    onTrackDeleteClicked()
                    openDropDownMenu = false
                }
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
        onTrackPicked = {},
        onTrackDeleteClicked = {},
        onTrackEditClicked = {}
    )
}

val testTracks: List<Track> = listOf(
    Track(artist = "Rush", title = "YYZ"),
    Track(artist = "Interpol", title = "PDA"),
    Track(
        artist = "Joan Jett Really Long Artist Name like what is happening",
        title = "Bad Reputation Really Long Song Name Please Clap"
    ),
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