package com.example.metronome.ui.tracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metronome.ui.AppViewModelProvider
import com.example.metronome.ui.components.TrackInputFormBody
import kotlinx.coroutines.launch

@Composable
fun TrackEditorScreen(
    onTrackUpdated: () -> Unit,
    viewModel: TrackEditorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    TrackInputFormBody(
        onSubmit = {
            coroutineScope.launch {
                viewModel.updateTrack()
                onTrackUpdated()
            }
        },
        trackUiState = viewModel.trackUiState,
        onValueChange = viewModel::updateUiState
    )
}