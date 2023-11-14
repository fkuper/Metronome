package com.example.metronome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.metronome.R
import com.example.metronome.utils.NoteValue

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
            imageVector = Icons.Rounded.MusicNote,
            contentDescription = stringResource(id = R.string.note_value_quarter),
            modifier = modifier
        )
        NoteValue.EIGHTH -> Icon(
            imageVector = Icons.Rounded.ShoppingCart,
            contentDescription = stringResource(id = R.string.note_value_eighth),
            modifier = modifier
        )
        NoteValue.SIXTEENTH -> Icon(
            imageVector = Icons.Rounded.Warning,
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
        LazyVerticalGrid(columns = GridCells.Fixed(NoteValue.values().size)) {
            items(NoteValue.values()) {
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