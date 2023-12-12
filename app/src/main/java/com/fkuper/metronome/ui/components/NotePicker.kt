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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.fkuper.metronome.R
import com.fkuper.metronome.utils.model.NoteValue

@Composable
fun NotePicker(
    noteValue: NoteValue,
    onNoteValuePicked: (NoteValue) -> Unit,
) {
    val openPickerDialog = remember { mutableStateOf(false) }

    NoteValueCard(
        noteValue = noteValue,
        onClick = {
            openPickerDialog.value = true
        }
    )

    if (openPickerDialog.value) {
        PickDialog(
            onDismissRequest = {
                openPickerDialog.value = false
            },
            onNoteValuePicked = {
                openPickerDialog.value = false
                onNoteValuePicked(it)
            }
        )
    }
}

@Composable
fun NoteIcon(
    noteValue: NoteValue,
    modifier: Modifier = Modifier
) {
    return when (noteValue) {
        NoteValue.QUARTER -> Icon(
            painter = painterResource(id = R.drawable.quarter_note),
            contentDescription = stringResource(id = R.string.note_value_quarter),
            modifier = modifier
        )
        NoteValue.EIGHTH -> Icon(
            painter = painterResource(id = R.drawable.eighth_note),
            contentDescription = stringResource(id = R.string.note_value_eighth),
            modifier = modifier
        )
        NoteValue.SIXTEENTH -> Icon(
            painter = painterResource(id = R.drawable.sixteenth_note),
            contentDescription = stringResource(id = R.string.note_value_sixteenth),
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteValueCard(
    noteValue: NoteValue,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        NoteIcon(
            noteValue = noteValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
private fun NoteValueGrid(onNoteValuePicked: (NoteValue) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.note_value_picker_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        LazyVerticalGrid(columns = GridCells.Fixed(NoteValue.entries.size)) {
            items(NoteValue.entries) {
                NoteValueCard(
                    noteValue = it,
                    onClick = {
                        onNoteValuePicked(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun PickDialog(
    onDismissRequest: () -> Unit,
    onNoteValuePicked: (NoteValue) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_large))
        ) {
            NoteValueGrid(onNoteValuePicked = onNoteValuePicked)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteValueGridPreview() {
    NoteValueGrid(onNoteValuePicked = {})
}