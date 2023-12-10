package com.fkuper.metronome.ui

import androidx.lifecycle.ViewModel
import com.chargemap.compose.numberpicker.Hours
import com.fkuper.metronome.data.SharedPreferencesRepository
import com.fkuper.metronome.utils.DisplayTheme
import com.fkuper.metronome.utils.Weekday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val prefRepo: SharedPreferencesRepository
) : ViewModel() {

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
        // TODO: set up alarm manager
    }

    fun setPracticeDays(newValue: Array<Weekday>) {
        prefRepo.setPracticeDays(newValue)
        _practiceDays.value = newValue
        // TODO: set up alarm manager
    }

    fun setPracticeHours(newValue: Hours) {
        prefRepo.setPracticeHours(newValue)
        _practiceHours.value = newValue
        // TODO: set up alarm manager
    }

    fun setDisplayTheme(newValue: DisplayTheme) {
        // it's committed into preferences in MainViewModel so only update UI state
        _displayTheme.value = newValue
    }

}