package com.fkuper.metronome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.fkuper.metronome.R
import com.fkuper.metronome.utils.model.NoteValue
import com.fkuper.metronome.utils.ext.TrackDetails
import com.fkuper.metronome.utils.ext.TrackUiState

@Composable
fun TrackInputFormBody(
    onSubmit: () -> Unit,
    trackUiState: TrackUiState,
    onValueChange: (TrackDetails) -> Unit,
) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TrackInputForm(
            trackDetails = trackUiState.trackDetails,
            onValueChange = onValueChange
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            onClick = onSubmit,
            enabled = trackUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
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