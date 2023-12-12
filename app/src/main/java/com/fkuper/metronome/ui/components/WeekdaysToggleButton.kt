package com.fkuper.metronome.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.fkuper.metronome.utils.model.Weekday

@Composable
fun WeekdaysToggleButton(
    onClick: (selectedDays: Array<Weekday>) -> Unit,
    modifier: Modifier = Modifier,
    selectedDays: Array<Weekday>? = Weekday.entries.toTypedArray(),
) {
    val days = Weekday.entries.toTypedArray()
    val selectedDaysState = remember(selectedDays) {
        mutableStateListOf<Weekday>().apply {
            selectedDays?.let { addAll(it) }
        }
    }

    Row(
        modifier = modifier
            .padding(0.dp)
            .height(50.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val onItemClicked: (text: Weekday) -> Unit = { day ->
            if (!selectedDaysState.contains(day)) {
                selectedDaysState.add(day)
            } else {
                selectedDaysState.remove(day)
            }
            onClick(selectedDaysState.toTypedArray())
        }

        val first = days.first()
        val last = days.last()
        val middle = days.slice(1..days.size - 2)

        WeekdaySelectionButton(
            day = first,
            selected = selectedDaysState.contains(first),
            modifier = Modifier
                .weight(1F)
                .clip(RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp)),
            onClick = onItemClicked
        )
        VerticalDivider()
        middle.map { day ->
            WeekdaySelectionButton(
                day = day,
                selected = selectedDaysState.contains(day),
                modifier = Modifier.weight(1F),
                onClick = onItemClicked
            )
            VerticalDivider()
        }
        WeekdaySelectionButton(
            day = last,
            selected = selectedDaysState.contains(last),
            modifier = Modifier
                .weight(1F)
                .clip(RoundedCornerShape(0.dp, 10.dp, 10.dp, 0.dp)),
            onClick = onItemClicked
        )
    }
}

@Composable
private fun WeekdaySelectionButton(
    day: Weekday,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (day: Weekday) -> Unit = {}
) {
    Button(
        onClick = { onClick(day) },
        colors = if (selected) {
            ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceTint)
        } else {
            ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)
        },
        shape = RoundedCornerShape(0),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = day.shortName,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.surfaceTint
            },
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Composable
private fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(2.dp)
    )
}