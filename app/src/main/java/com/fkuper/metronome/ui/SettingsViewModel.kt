package com.fkuper.metronome.ui

import androidx.lifecycle.AndroidViewModel
import com.chargemap.compose.numberpicker.Hours
import com.fkuper.metronome.MetronomeApplication
import com.fkuper.metronome.utils.model.DisplayTheme
import com.fkuper.metronome.utils.PracticeNotificationsAlarmHandler
import com.fkuper.metronome.utils.model.Weekday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: MetronomeApplication) : AndroidViewModel(application) {

    private val prefRepo = application.container.preferencesRepository
    private val notificationsAlarmHandler = PracticeNotificationsAlarmHandler.getInstance(application.applicationContext)

    private val _practiceRemindersOn = MutableStateFlow(prefRepo.getPracticeRemindersOn())
    val practiceRemindersOn = _practiceRemindersOn.asStateFlow()

    private val _practiceDays = MutableStateFlow(prefRepo.getPracticeDays())
    val practiceDays = _practiceDays.asStateFlow()

    private val _practiceHours = MutableStateFlow(prefRepo.getPracticeHours())
    val practiceHours = _practiceHours.asStateFlow()

    private val _displayTheme = MutableStateFlow(prefRepo.getDisplayTheme())
    val displayTheme = _displayTheme.asStateFlow()

    fun togglePracticeReminders(newValue: Boolean) {
        prefRepo.setPracticeRemindersOn(newValue)
        _practiceRemindersOn.value = newValue
        notificationsAlarmHandler.setupPracticeNotifications()
    }

    fun setPracticeDays(newValue: Array<Weekday>) {
        prefRepo.setPracticeDays(newValue)
        _practiceDays.value = newValue
        notificationsAlarmHandler.setupPracticeNotifications()
    }

    fun setPracticeHours(newValue: Hours) {
        prefRepo.setPracticeHours(newValue)
        _practiceHours.value = newValue
        notificationsAlarmHandler.setupPracticeNotifications()
    }

    fun setDisplayTheme(newValue: DisplayTheme) {
        // it's committed into preferences in MainViewModel so only update UI state
        _displayTheme.value = newValue
    }

}