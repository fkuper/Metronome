package com.example.metronome.ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.metronome.R

private const val BPM_LOWER_BOUND = 1
private const val BPM_UPPER_BOUND = 400
private const val BPM_STEPPER_VALUE = 10

@Composable
fun BpmPicker(
    bpm: Int,
    onBpmChanged: (Int) -> Unit,
) {
    val bpmState = remember { mutableIntStateOf(bpm) }
    val possibleValues = (BPM_LOWER_BOUND..BPM_UPPER_BOUND).toList()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        IconButton(
            onClick = {
                decrement(bpmState)
                onBpmChanged(bpmState.intValue)
            },
            enabled = bpmState.intValue > BPM_LOWER_BOUND
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = stringResource(id = R.string.bpm_decrement)
            )
        }
        ListItemPicker(
            value = bpmState.intValue,
            onValueChange = {
                bpmState.intValue = it
                onBpmChanged(it)
            },
            list = possibleValues,
            dividersColor = MaterialTheme.colorScheme.surfaceTint,
            textStyle = MaterialTheme.typography.titleLarge,
            modifier = Modifier.width(dimensionResource(id = R.dimen.bpm_picker_width))
        )
        IconButton(
            onClick = {
                increment(bpmState)
                onBpmChanged(bpmState.intValue)
            },
            enabled = bpmState.intValue < BPM_UPPER_BOUND
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = stringResource(id = R.string.bpm_increment)
            )
        }
    }
}

private fun increment(bpmState: MutableIntState) {
    if (bpmState.intValue >= (BPM_UPPER_BOUND - BPM_STEPPER_VALUE)) {
        bpmState.intValue = BPM_UPPER_BOUND
    } else {
        bpmState.intValue += BPM_STEPPER_VALUE
    }
}

private fun decrement(bpmState: MutableIntState) {
    if (bpmState.intValue <= (BPM_LOWER_BOUND + BPM_STEPPER_VALUE)) {
        bpmState.intValue = BPM_LOWER_BOUND
    } else {
        bpmState.intValue -= BPM_STEPPER_VALUE
    }
}

@Preview(showBackground = true)
@Composable
fun BpmPickerPreview() {
    BpmPicker(120, onBpmChanged = {})
}