package com.fkuper.metronome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.fkuper.metronome.R
import com.fkuper.metronome.utils.model.TimeSignature

@Composable
fun TimeSignaturePicker(
    timeSignature: TimeSignature,
    onTimeSignaturePicked: (TimeSignature) -> Unit
) {
    val openPickerDialog = remember { mutableStateOf(false) }

    TimeSignatureCard(
        timeSignature = timeSignature,
        onClick = {
            openPickerDialog.value = true
        }
    )
    
    if (openPickerDialog.value) {
        PickDialog(
            onDismissRequest = {
                openPickerDialog.value = false
            },
            onTimeSignaturePicked = {
                openPickerDialog.value = false
                onTimeSignaturePicked(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSignatureCard(
    timeSignature: TimeSignature,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = "${timeSignature.upper}/${timeSignature.lower}",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
private fun TimeSignatureGrid(onTimeSignaturePicked: (TimeSignature) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.time_signature_picker_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        LazyVerticalGrid(columns = GridCells.Fixed(4)) {
            items(TimeSignature.values()) {
                TimeSignatureCard(
                    timeSignature = it,
                    onClick = {
                        onTimeSignaturePicked(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun PickDialog(
    onDismissRequest: () -> Unit,
    onTimeSignaturePicked: (TimeSignature) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_large))
        ) {
            TimeSignatureGrid(onTimeSignaturePicked = onTimeSignaturePicked)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeSignatureGridPreview() {
    TimeSignatureGrid(onTimeSignaturePicked = {})
}