package com.example.metronome.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metronome.utils.NoteValue

@Composable
fun NotePicker(
    noteValue: NoteValue,
    onNoteValuePicked: (NoteValue) -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: implement note value picker
    GetNoteIcon(noteValue)
}

@Composable
private fun GetNoteIcon(noteValue: NoteValue) {
    return when (noteValue) {
        NoteValue.QUARTER -> Icon(Icons.Rounded.MusicNote, "quarter notes")
        NoteValue.EIGHTH -> Icon(Icons.Rounded.ShoppingCart, "eighth notes")
        NoteValue.SIXTEENTH -> Icon(Icons.Rounded.Warning, "sixteenth notes")
    }
}