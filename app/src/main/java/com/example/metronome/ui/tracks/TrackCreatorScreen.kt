package com.example.metronome.ui.tracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metronome.ui.AppViewModelProvider
import com.example.metronome.ui.components.TrackInputFormBody
import com.example.metronome.utils.TrackUiState
import kotlinx.coroutines.launch

@Composable
fun TrackCreatorScreen(
    onTrackCreated: () -> Unit,
    viewModel: TrackCreatorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    TrackInputFormBody(
        onSubmit = {
            coroutineScope.launch {
                viewModel.saveTrack()
                onTrackCreated()
            }
        },
        trackUiState = viewModel.trackUiState,
        onValueChange = viewModel::updateUiState
    )
}

@Preview(showBackground = true)
@Composable
private fun TrackCreatorScreenPreview() {
    TrackInputFormBody(onSubmit = {}, trackUiState = TrackUiState(), onValueChange = {})
}