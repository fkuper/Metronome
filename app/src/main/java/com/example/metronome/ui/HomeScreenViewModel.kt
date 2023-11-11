package com.example.metronome.ui

import androidx.lifecycle.ViewModel
import com.example.metronome.utils.MetronomeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel: ViewModel() {

    private val _metronomeConfig = MutableStateFlow(MetronomeConfig())
    val metronomeConfig = _metronomeConfig.asStateFlow()

}