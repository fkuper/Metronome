package com.example.metronome.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metronome.utils.TimeSignature

@Composable
fun TimeSignaturePicker(
    timeSignature: TimeSignature,
    onTimeSignaturePicked: (TimeSignature) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: implement time signature picker
    Text(
        "${timeSignature.upper}/${timeSignature.lower}",
        modifier = modifier
    )
}