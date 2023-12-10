package com.fkuper.metronome

import androidx.lifecycle.ViewModel
import com.fkuper.metronome.data.SharedPreferencesRepository
import com.fkuper.metronome.utils.DisplayTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val prefRepo: SharedPreferencesRepository) : ViewModel() {

    private val _displayTheme = MutableStateFlow(prefRepo.getDisplayTheme())
    val displayTheme = _displayTheme.asStateFlow()

    fun setDisplayTheme(newValue: DisplayTheme) {
        prefRepo.setDisplayTheme(newValue)
        _displayTheme.value = newValue
    }

}