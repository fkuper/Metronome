package com.example.metronome.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chargemap.compose.numberpicker.NumberPicker

@Composable
fun BpmPicker(
    bpm: Int,
    onBpmChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var bpmState = remember { mutableIntStateOf(bpm) }

    NumberPicker(
        value = bpmState.intValue,
        onValueChange = {
            bpmState.intValue = it
            onBpmChanged(it)
        },
        range = 1..400,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun BpmPickerPreview() {
    BpmPicker(120, onBpmChanged = {})
}