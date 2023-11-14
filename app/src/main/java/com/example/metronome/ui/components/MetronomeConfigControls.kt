package com.example.metronome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.metronome.R
import com.example.metronome.utils.NoteValue
import com.example.metronome.utils.TimeSignature

@Composable
fun MetronomeConfigControls(
    bpm: Int = 120,
    timeSignature: TimeSignature = TimeSignature.FOUR_FOUR,
    noteValue: NoteValue = NoteValue.QUARTER,
    onBpmChanged: (Int) -> Unit,
    onTimeSignaturePicked: (TimeSignature) -> Unit,
    onNoteValuePicked: (NoteValue) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BpmPicker(
            bpm = bpm,
            onBpmChanged = onBpmChanged,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            TimeSignaturePicker(
                timeSignature = timeSignature,
                onTimeSignaturePicked = onTimeSignaturePicked
            )
            Spacer(modifier = Modifier.height(20.dp))
            NotePicker(
                noteValue = noteValue,
                onNoteValuePicked = onNoteValuePicked
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MetronomeConfigControlsPreview() {
    MetronomeConfigControls(onBpmChanged = {}, onTimeSignaturePicked = {}, onNoteValuePicked = {})
}