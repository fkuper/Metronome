package com.fkuper.metronome.ui.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fkuper.metronome.R
import com.fkuper.metronome.ui.AppViewModelProvider
import com.fkuper.metronome.ui.components.TrackTitleAndArtistColumn
import kotlinx.coroutines.launch

@Composable
fun TrackSearcherScreen(
    viewModel: TrackSearcherViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snackbarHostState: SnackbarHostState
) {
    val viewModelScope = rememberCoroutineScope()
    val searchState by viewModel.searchState.collectAsState()
    val addTrackState by viewModel.addTrackState.collectAsState()

    TrackSearcherScreenBody(
        tracks = viewModel.tracksMap.values.toList(),
        onSearchForTracks = {
            viewModelScope.launch {
                viewModel.searchForTrackByTitle(it)
            }
        },
        onAddClicked = {
            viewModelScope.launch {
                viewModel.addTrackToPlaylist(it.spotifyTrack)
            }
        },
        onRemoveClicked = {
            viewModelScope.launch {
                viewModel.removeTrackFromPlaylist(it.spotifyTrack)
            }
        },
        searchState = searchState,
        addTrackState = addTrackState,
        snackbarHostState = snackbarHostState,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun TrackSearcherScreenBody(
    tracks: List<SpotifyTrackUiState>?,
    searchState: SearchState,
    addTrackState: AddSpotifyTrackState,
    snackbarHostState: SnackbarHostState,
    onSearchForTracks: (String) -> Unit,
    onAddClicked: (SpotifyTrackUiState) -> Unit,
    onRemoveClicked: (SpotifyTrackUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        TrackSearchBar(
            onSearchForTracks = { onSearchForTracks(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )

        searchState.let { state ->
            when (state) {
                SearchState.START -> { /* don't show anything yet */ }
                SearchState.LOADING -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                SearchState.SUCCESS -> {
                    if (tracks == null) return
                    LazyColumn {
                        items(tracks) { track ->
                            SpotifyTrackRow(
                                track = track,
                                onAddClicked = { onAddClicked(track) },
                                onRemoveClicked = { onRemoveClicked(track) },
                                addTrackState = addTrackState
                            )
                        }
                    }
                }
                is SearchState.FAILURE -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Something went wrong: ${state.message}")
                    }
                }
            }
        }
    }
}

@Composable
private fun SpotifyTrackRow(
    track: SpotifyTrackUiState,
    addTrackState: AddSpotifyTrackState,
    onAddClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
) {
    // TODO: use addTrackState here to show message on failure to add track and loading indicator

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        AlbumArtView(url = track.spotifyTrack.album.images.first().url)
        TrackTitleAndArtistCard(
            title = track.spotifyTrack.title,
            artist = track.spotifyTrack.artists.first().name,
            onInteractionButtonClicked = {
                if (!track.isInPlaylist) {
                    onAddClicked()
                } else {
                    onRemoveClicked()
                }
            },
            interactionButtonIcon =
                if (!track.isInPlaylist) {
                    Icons.Rounded.Add
                } else {
                    Icons.Rounded.Remove
                }
        )
    }
}

@Composable
private fun TrackTitleAndArtistCard(
    title: String,
    artist: String,
    onInteractionButtonClicked: () -> Unit,
    interactionButtonIcon: ImageVector
) {
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrackTitleAndArtistColumn(
                title = title,
                artist = artist,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
            )
            IconButton(onClick = onInteractionButtonClicked) {
                Icon(imageVector = interactionButtonIcon, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackSearchBar(
    onSearchForTracks: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchString by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = searchString,
        onValueChange = { searchString = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search_prompt_icon)
            )
        },
        label = {
            Text(stringResource(id = R.string.search_prompt))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (searchString.isNotBlank()) {
                onSearchForTracks(searchString)
            }
            focusManager.clearFocus()
        }),
        modifier = modifier
    )
}

@Composable
private fun AlbumArtView(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(10)
            )
    )
}