package com.fkuper.metronome.ui.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fkuper.metronome.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun TrackSearcherScreen(
    viewModel: TrackSearcherViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val viewModelScope = rememberCoroutineScope()

    TrackSearcherScreenBody(
        modifier = Modifier.fillMaxSize(),
        onSearchForTracks = {
            viewModelScope.launch {
                viewModel.searchForTrackByTitle(it)
            }
        }
    )
}

@Composable
private fun TrackSearcherScreenBody(
    modifier: Modifier = Modifier,
    onSearchForTracks: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Text("Search bar here")
        Button(onClick = { onSearchForTracks("test") }) {
            Text("Search for tracks")
        }
        Text("List of Songs here")
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackSearcherScreenBodyPreview() {
    TrackSearcherScreenBody(
        modifier = Modifier.fillMaxSize(),
        onSearchForTracks = {}
    )
}