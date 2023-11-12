package com.example.metronome.utils

import androidx.annotation.StringRes
import com.example.metronome.R

enum class MetronomeScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    TrackPicker(title = R.string.track_picker),
    TrackSearcher(title = R.string.track_searcher),
    TrackCreator(title = R.string.track_creator),
    Settings(title = R.string.settings)
}