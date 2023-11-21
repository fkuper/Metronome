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
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
    val tracksState by viewModel.tracksState.collectAsState()

    TrackSearcherScreenBody(
        tracks = tracksState.values.toList(),
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
        onFailureMessage = {
            viewModelScope.launch {
                viewModel.failureMessageShown(it)
                if (it.failureMessage == null) return@launch
                snackbarHostState.showSnackbar(it.failureMessage)
            }
        },
        onSearchFailure = {
            viewModelScope.launch {
                snackbarHostState.showSnackbar("Something went wrong: $it")
            }
        },
        onAddSuccess = {
            viewModelScope.launch {
                viewModel.successMessageShown(it)
                snackbarHostState.showSnackbar("${it.spotifyTrack.title} has been added to your playlist!")
            }
        },
        onRemoveSuccess = {
            viewModelScope.launch {
                viewModel.removeMessageShown(it)
                snackbarHostState.showSnackbar("${it.spotifyTrack.title} has been removed from your playlist.")
            }
        },
        searchState = searchState,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun TrackSearcherScreenBody(
    tracks: List<SpotifyTrackUiState>?,
    searchState: SearchState,
    onSearchForTracks: (String) -> Unit,
    onAddClicked: (SpotifyTrackUiState) -> Unit,
    onRemoveClicked: (SpotifyTrackUiState) -> Unit,
    onFailureMessage: (SpotifyTrackUiState) -> Unit,
    onAddSuccess: (SpotifyTrackUiState) -> Unit,
    onRemoveSuccess: (SpotifyTrackUiState) -> Unit,
    onSearchFailure: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                                onRemoveClicked = { onRemoveClicked(track) }
                            )

                            if (track.failureMessage != null) {
                                onFailureMessage(track)
                            }
                            if (track.justAdded) {
                                onAddSuccess(track)
                            }
                            if (track.justRemoved) {
                                onRemoveSuccess(track)
                            }
                        }
                    }
                }
                is SearchState.FAILURE -> {
                    state.message?.let { onSearchFailure(it) }
                }
            }
        }
    }
}

@Composable
private fun SpotifyTrackRow(
    track: SpotifyTrackUiState,
    onAddClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        AlbumArtView(url = track.spotifyTrack.album.images.first().url)
        TrackTitleAndArtistCard(
            track = track,
            onAddButtonClicked = onAddClicked,
            onRemoveButtonClicked = onRemoveClicked
        )
    }
}

@Composable
private fun TrackTitleAndArtistCard(
    track: SpotifyTrackUiState,
    onAddButtonClicked: () -> Unit,
    onRemoveButtonClicked: () -> Unit
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
                title = track.spotifyTrack.title,
                artist = track.spotifyTrack.artists.first().name,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
            )

            if (track.isInPlaylist) {
                IconButton(onClick = onRemoveButtonClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        contentDescription = stringResource(id = R.string.remove_track_from_playlist)
                    )
                }
            } else {
                IconButton(onClick = onAddButtonClicked) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        contentDescription = stringResource(id = R.string.add_track_to_playlist)
                    )
                }
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