package com.fkuper.metronome.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.FlagCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fkuper.metronome.R
import com.fkuper.metronome.ui.components.MetronomeConfigControls
import com.fkuper.metronome.utils.model.MetronomeConfig
import com.fkuper.metronome.utils.model.NoteValue
import com.fkuper.metronome.utils.model.TimeSignature

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val metronomeConfig by viewModel.metronomeConfig.collectAsState()
    val metronomeIsPlaying by viewModel.metronomeIsPlaying.collectAsState()
    val metronomeCount by viewModel.metronomeTickCounter.collectAsState()

    HomeScreenBody(
        metronomeConfig = metronomeConfig,
        metronomeIsPlaying = metronomeIsPlaying,
        metronomeCount = metronomeCount,
        onPlayPressed = { viewModel.startMetronomeService() },
        onStopPressed = { viewModel.stopMetronomeService() },
        onBpmChanged = { viewModel.updateMetronomeConfig(metronomeConfig.copy(bpm = it)) },
        onTimeSignatureChanged = { viewModel.updateMetronomeConfig(metronomeConfig.copy(timeSignature = it)) },
        onNoteValuePicked = { viewModel.updateMetronomeConfig(metronomeConfig.copy(noteValue = it)) }
    )
}

@Composable
private fun HomeScreenBody(
    metronomeConfig: MetronomeConfig,
    metronomeIsPlaying: Boolean,
    metronomeCount: Int,
    onPlayPressed: () -> Unit,
    onStopPressed: () -> Unit,
    onBpmChanged: (Int) -> Unit,
    onTimeSignatureChanged: (TimeSignature) -> Unit,
    onNoteValuePicked: (NoteValue) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        MetronomeTickView(
            timeSignature = metronomeConfig.timeSignature,
            tickCount = metronomeCount,
            modifier = Modifier.fillMaxWidth().weight(1F)
        )
        MetronomePlayButtonBar(
            metronomeIsPlaying = metronomeIsPlaying,
            onPlayPressed = onPlayPressed,
            onStopPressed = onStopPressed
        )
        MetronomeConfigControls(
            bpm = metronomeConfig.bpm,
            timeSignature = metronomeConfig.timeSignature,
            noteValue = metronomeConfig.noteValue,
            onBpmChanged = { onBpmChanged(it) },
            onTimeSignaturePicked = { onTimeSignatureChanged(it) },
            onNoteValuePicked = { onNoteValuePicked(it) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MetronomeTickView(
    timeSignature: TimeSignature,
    tickCount: Int,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = 4
    ) {
        repeat(timeSignature.upper) {
            val imageVector =
                if (it == 0) Icons.Rounded.FlagCircle
                else Icons.Rounded.Circle
            val tint =
                if (it == tickCount) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary

            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp),
                tint = tint
            )
        }
    }
}

@Composable
private fun MetronomePlayButtonBar(
    metronomeIsPlaying: Boolean,
    onPlayPressed: () -> Unit,
    onStopPressed: () -> Unit
) {
    val buttonModifier = Modifier
        .size(80.dp)
        .border(
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline),
            shape = CircleShape
        )
        .shadow(4.dp, shape = CircleShape)
        .background(MaterialTheme.colorScheme.background)

    Box(contentAlignment = Alignment.Center) {
        Divider(modifier = Modifier.fillMaxWidth(), thickness = dimensionResource(id = R.dimen.border_width_medium))
        if (!metronomeIsPlaying) {
            IconButton(onClick = onPlayPressed, modifier = buttonModifier) {
                Icon(
                    imageVector = Icons.Rounded.PlayCircle,
                    contentDescription = null,
                    modifier = buttonModifier,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            IconButton(onClick = onStopPressed, modifier = buttonModifier) {
                Icon(
                    imageVector = Icons.Rounded.StopCircle,
                    contentDescription = null,
                    modifier = buttonModifier,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenBodyPreview() {
    HomeScreenBody(
        metronomeConfig = MetronomeConfig(),
        metronomeIsPlaying = false,
        metronomeCount = 0,
        onPlayPressed = { },
        onStopPressed = { },
        onBpmChanged = { },
        onTimeSignatureChanged = { },
        onNoteValuePicked = { }
    )
}