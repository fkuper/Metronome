package com.example.metronome.ui.home

import androidx.lifecycle.ViewModel
import com.example.metronome.utils.MetronomeConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {

    private val _metronomeConfig = MutableStateFlow(MetronomeConfig())
    val metronomeConfig = _metronomeConfig.asStateFlow()

}