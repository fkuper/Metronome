package com.fkuper.metronome.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DisplaySettings
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import com.fkuper.metronome.R
import com.fkuper.metronome.ui.components.WeekdaysToggleButton
import com.fkuper.metronome.utils.DisplayTheme
import com.fkuper.metronome.utils.Weekday

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onThemeChanged: (DisplayTheme) -> Unit
) {
    val practiceRemindersOn by viewModel.practiceRemindersOn.collectAsState()
    val practiceDays by viewModel.practiceDays.collectAsState()
    val practiceHours by viewModel.practiceHours.collectAsState()
    val displayTheme by viewModel.displayTheme.collectAsState()

    SettingsScreenBody(
        practiceRemindersOn = practiceRemindersOn,
        onPracticeRemindersToggled = {
            viewModel.togglePracticeReminders(it)
        },
        practiceDays = practiceDays,
        onPracticeDaysChanged = {
            viewModel.setPracticeDays(it)
        },
        practiceHours = practiceHours,
        onPracticeHoursChanged = {
            viewModel.setPracticeHours(it)
        },
        displayTheme = displayTheme,
        onDisplayThemeChanged = {
            viewModel.setDisplayTheme(it)
            onThemeChanged(it)
        },
        modifier = Modifier
    )
}

@Composable
private fun SettingsScreenBody(
    practiceRemindersOn: Boolean,
    onPracticeRemindersToggled: (Boolean) -> Unit,
    practiceDays: Array<Weekday>?,
    onPracticeDaysChanged: (Array<Weekday>) -> Unit,
    practiceHours: Hours,
    onPracticeHoursChanged: (Hours) -> Unit,
    displayTheme: DisplayTheme?,
    onDisplayThemeChanged: (DisplayTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        SectionHeading(icon = Icons.Rounded.Notifications, title = "Notifications")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = "Practice Reminders",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1F))
            Switch(
                checked = practiceRemindersOn,
                onCheckedChange = { onPracticeRemindersToggled(it) },
                thumbContent = if (practiceRemindersOn) {
                    {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                } else null
            )
            IconButton(onClick = {
                // TODO: implement info popup to tell user about practice reminder notifications
            }) {
                Icon(imageVector = Icons.Rounded.QuestionMark, contentDescription = null)
            }
        }
        if (practiceRemindersOn) {
            PracticeRemindersSetup(
                practiceDays = practiceDays,
                onPracticeDaysChanged = onPracticeDaysChanged,
                practiceHours = practiceHours,
                onPracticeHoursChanged = onPracticeHoursChanged,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
            )
        }
        SectionHeading(icon = Icons.Rounded.DisplaySettings, title = "Display")
        DisplayThemePicker(
            displayTheme = displayTheme ?: DisplayTheme.AUTO,
            onDisplayThemeChanged = { onDisplayThemeChanged(it) }
        )
        SectionHeading(icon = Icons.Rounded.Info, title = "Info")
        // TODO: implement info section (attributions, info about me etc.)
    }
}

@Composable
private fun DisplayThemePicker(
    modifier: Modifier = Modifier,
    displayTheme: DisplayTheme,
    onDisplayThemeChanged: (DisplayTheme) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DisplayTheme.entries.forEach {
            DisplayThemePickerItem(
                theme = it,
                selected = displayTheme == it,
                onClick = { onDisplayThemeChanged(it) }
            )
        }
    }
}

@Composable
private fun DisplayThemePickerItem(
    selected: Boolean,
    onClick: () -> Unit,
    theme: DisplayTheme
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Icon(imageVector = theme.icon, contentDescription = null)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = theme.text)
    }
}

@Composable
private fun PracticeRemindersSetup(
    modifier: Modifier = Modifier,
    practiceDays: Array<Weekday>?,
    onPracticeDaysChanged: (Array<Weekday>) -> Unit,
    practiceHours: Hours,
    onPracticeHoursChanged: (Hours) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pick your practice days",
            style = MaterialTheme.typography.bodyLarge
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.padding_small))
        )
        WeekdaysToggleButton(
            onClick = { onPracticeDaysChanged(it) },
            selectedDays = practiceDays
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Pick your practice time",
            style = MaterialTheme.typography.bodyLarge
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.padding_small))
        )
        TimePicker(
            hours = practiceHours,
            onValueChange = { onPracticeHoursChanged(it) }
        )
    }
}

@Composable
private fun SectionHeading(icon: ImageVector, title: String) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun TimePicker(
    modifier: Modifier = Modifier,
    hours: Hours = FullHours(19, 30),
    onValueChange: (Hours) -> Unit,
) {
    HoursNumberPicker(
        modifier = modifier,
        dividersColor = MaterialTheme.colorScheme.primary,
        leadingZero = false,
        value = hours,
        onValueChange = { onValueChange(it) },
        hoursDivider = {
            Text(
                modifier = Modifier.size(24.dp),
                textAlign = TextAlign.Center,
                text = ":"
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenBody(
        practiceRemindersOn = true,
        onPracticeRemindersToggled = {},
        practiceDays = arrayOf(Weekday.FRIDAY),
        onPracticeDaysChanged = {},
        practiceHours = FullHours(19, 30),
        onPracticeHoursChanged = {},
        displayTheme = DisplayTheme.AUTO,
        onDisplayThemeChanged = {}
    )
}