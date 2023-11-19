package com.fkuper.metronome.ui.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TrackSearcherScreen(

) {
    TrackSearcherScreenBody(modifier = Modifier.fillMaxSize())
}

@Composable
private fun TrackSearcherScreenBody(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Text("Search bar here")
        Text("List of Songs here")
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackSearcherScreenBodyPreview() {
    TrackSearcherScreenBody()
}