package com.example.metronome.ui.tracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metronome.R
import com.example.metronome.ui.AppViewModelProvider
import com.example.metronome.ui.components.MetronomeConfigControls
import com.example.metronome.utils.NoteValue
import kotlinx.coroutines.launch

@Composable
fun TrackCreatorScreen(
    onTrackCreated: () -> Unit,
    viewModel: TrackCreatorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    TrackCreatorBody(
        onTrackCreated = {
            coroutineScope.launch {
                viewModel.saveTrack()
                onTrackCreated()
            }
        },
        trackDetails = viewModel.trackUiState.trackDetails,
        onValueChange = viewModel::updateUiState
    )
}

@Composable
private fun TrackCreatorBody(
    onTrackCreated: () -> Unit,
    trackDetails: TrackDetails,
    onValueChange: (TrackDetails) -> Unit,
) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TrackInputForm(
            trackDetails = trackDetails,
            onValueChange = onValueChange
        )
        Button(onClick = onTrackCreated) {
            Text("Save Item")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackInputForm(
    trackDetails: TrackDetails,
    onValueChange: (TrackDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = trackDetails.title,
            onValueChange = { onValueChange(trackDetails.copy(title = it)) },
            label = { Text(stringResource(id = R.string.title_input_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = trackDetails.artist,
            onValueChange = { onValueChange(trackDetails.copy(artist = it)) },
            label = { Text(stringResource(id = R.string.artist_input_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        MetronomeConfigControls(
            bpm = trackDetails.bpm,
            timeSignature = trackDetails.timeSignature,
            noteValue = trackDetails.noteValue ?: NoteValue.QUARTER,
            onBpmChanged = { onValueChange(trackDetails.copy(bpm = it)) },
            onTimeSignaturePicked = { onValueChange(trackDetails.copy(timeSignature = it)) },
            onNoteValuePicked = { onValueChange(trackDetails.copy(noteValue = it)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackCreatorScreenPreview() {
    TrackCreatorBody(onTrackCreated = {}, trackDetails = TrackDetails(), onValueChange = {})
}