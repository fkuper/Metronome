package com.fkuper.metronome.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DisplaySettings
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import com.fkuper.metronome.R
import com.fkuper.metronome.ui.components.WeekdaysToggleButton
import com.fkuper.metronome.utils.model.DisplayTheme
import com.fkuper.metronome.utils.model.Weekday

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
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        PracticeRemindersInfoDialog(onDismissRequest = {
            showInfoDialog = false
        })
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        SectionHeading(icon = Icons.Rounded.Notifications, title = "Notifications")
        PracticeRemindersNotificationPicker(
            practiceRemindersOn = practiceRemindersOn,
            onPracticeRemindersToggled = onPracticeRemindersToggled,
            practiceDays = practiceDays,
            onPracticeDaysChanged = onPracticeDaysChanged,
            practiceHours = practiceHours,
            onPracticeHoursChanged = onPracticeHoursChanged
        )
        SectionHeading(icon = Icons.Rounded.DisplaySettings, title = "Display")
        DisplayThemePicker(
            displayTheme = displayTheme ?: DisplayTheme.AUTO,
            onDisplayThemeChanged = { onDisplayThemeChanged(it) }
        )
        SectionHeading(icon = Icons.Rounded.Info, title = "Info")
        InfoSection()
    }
}

@Composable
private fun InfoSection() {
    val uriHandler = LocalUriHandler.current
    val authorMail = stringResource(id = R.string.author_email_uri)
    val githubUrl = stringResource(id = R.string.github_url)
    val iconCredit = stringResource(id = R.string.flaticon_pentagram_icons_uri)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        InfoSectionItem(
            title = "Author",
            body = "Frederik Kuper",
            onClick = { uriHandler.openUri(authorMail) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        InfoSectionItem(
            title = "Source Code",
            body = "github.com/fkuper/Metronome",
            onClick = { uriHandler.openUri(githubUrl) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        InfoSectionItem(
            title = "Note Icons Credit",
            body = "Pentagram icons created by Freepik - Flaticon",
            onClick = { uriHandler.openUri(iconCredit) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun InfoSectionItem(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = body, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun PracticeRemindersNotificationPicker(
    practiceRemindersOn: Boolean,
    onPracticeRemindersToggled: (Boolean) -> Unit,
    practiceDays: Array<Weekday>?,
    onPracticeDaysChanged: (Array<Weekday>) -> Unit,
    practiceHours: Hours,
    onPracticeHoursChanged: (Hours) -> Unit
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        PracticeRemindersInfoDialog(onDismissRequest = {
            showInfoDialog = false
        })
    }

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
        IconButton(onClick = { showInfoDialog = true }) {
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
}

@Composable
private fun PracticeRemindersInfoDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(id = R.string.practice_reminder_info_text),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                textAlign = TextAlign.Center
            )
        }
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
        },
        textStyle = MaterialTheme.typography.bodyLarge
            .copy(color = MaterialTheme.colorScheme.primary)
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