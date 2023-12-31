package com.fkuper.metronome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.fkuper.metronome.R
import com.fkuper.metronome.utils.model.NoteValue
import com.fkuper.metronome.utils.model.TimeSignature

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
            onBpmChanged = onBpmChanged
        )
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            TimeSignaturePicker(
                timeSignature = timeSignature,
                onTimeSignaturePicked = onTimeSignaturePicked
            )
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