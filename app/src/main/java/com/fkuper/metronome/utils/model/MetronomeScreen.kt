package com.fkuper.metronome.utils.model

import androidx.annotation.StringRes
import com.fkuper.metronome.R

enum class MetronomeScreen(@StringRes val title: Int, val navArgumentName: String? = null) {
    Home(title = R.string.app_name),
    TrackPicker(title = R.string.track_picker),
    TrackSearcher(title = R.string.track_searcher),
    TrackCreator(title = R.string.track_creator),
    TrackEditor(title = R.string.track_editor, navArgumentName = "trackId"),
    Settings(title = R.string.settings);

    override fun toString(): String {
        if (navArgumentName != null) {
            return this.name + "/{$navArgumentName}"
        }
        return this.name
    }

    companion object {
        fun forString(input: String): MetronomeScreen? {
            return entries.find { it.toString() == input }
        }
    }
}