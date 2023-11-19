package com.fkuper.metronome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.fkuper.metronome.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackRow(
    title: String,
    artist: String,
    onCardClicked: () -> Unit = {},
    onInteractionButtonClicked: () -> Unit,
    interactionButtonIcon: ImageVector,
    detailView: @Composable () -> Unit = {},
    buttonDropDown: @Composable () -> Unit = {}
) {
    Card(
        onClick = { onCardClicked() },
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrackTitleAndArtistColumn(
                title = title,
                artist = artist,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
            )
            detailView()
            IconButton(onClick = onInteractionButtonClicked) {
                Icon(imageVector = interactionButtonIcon, contentDescription = null)
                buttonDropDown()
            }
        }
    }
}

@Composable
private fun TrackTitleAndArtistColumn(
    title: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Text(
            text = artist,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}