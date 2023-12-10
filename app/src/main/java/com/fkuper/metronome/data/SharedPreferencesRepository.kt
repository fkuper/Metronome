package com.fkuper.metronome.data

import android.content.Context
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.fkuper.metronome.utils.DisplayTheme
import com.fkuper.metronome.utils.Weekday
import com.google.gson.Gson

class SharedPreferencesRepository(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()
    private val gson = Gson()

    fun setPracticeRemindersOn(value: Boolean) {
        PREF_KEY_REMINDERS_ON.put(value)
    }

    fun getPracticeRemindersOn(): Boolean {
        return PREF_KEY_REMINDERS_ON.getBoolean()
    }

    fun setPracticeDays(value: Array<Weekday>) {
        PREF_KEY_PRACTICE_DAYS.put(gson.toJson(value))
    }

    fun getPracticeDays(): Array<Weekday>? {
        PREF_KEY_PRACTICE_DAYS.getString().also {
            return if (it.isNotEmpty()) {
                gson.fromJson(PREF_KEY_PRACTICE_DAYS.getString(), Array<Weekday>::class.java)
            } else {
                null
            }
        }
    }

    fun setPracticeHours(value: Hours) {
        PREF_KEY_PRACTICE_TIME_HOURS.put(value.hours)
        PREF_KEY_PRACTICE_TIME_MINUTES.put(value.minutes)
    }

    fun getPracticeHours(): Hours {
        val hours = PREF_KEY_PRACTICE_TIME_HOURS.getInt()
        val minutes = PREF_KEY_PRACTICE_TIME_MINUTES.getInt()

        return FullHours(hours, minutes)
    }

    fun setDisplayTheme(value: DisplayTheme) {
        PREF_KEY_DISPLAY_THEME.put(gson.toJson(value))
    }

    fun getDisplayTheme(): DisplayTheme? {
        PREF_KEY_DISPLAY_THEME.getString().also {
            return if (it.isNotEmpty()) {
                gson.fromJson(PREF_KEY_DISPLAY_THEME.getString(), DisplayTheme::class.java)
            } else {
                null
            }
        }
    }

    private fun String.put(value: String) {
        editor.putString(this, value)
        editor.commit()
    }

    private fun String.put(value: Boolean) {
        editor.putBoolean(this, value)
        editor.commit()
    }

    private fun String.put(value: Int) {
        editor.putInt(this, value)
        editor.commit()
    }

    private fun String.getBoolean() = preferences.getBoolean(this, false)
    private fun String.getInt() = preferences.getInt(this, 0)
    private fun String.getString() = preferences.getString(this, "")!!

    companion object {
        private const val PREFERENCES_NAME = "metronome_preferences"

        private const val PREF_KEY_REMINDERS_ON = "practice_reminders_on"
        private const val PREF_KEY_PRACTICE_DAYS = "practice_days"
        private const val PREF_KEY_PRACTICE_TIME_HOURS = "practice_time_hours"
        private const val PREF_KEY_PRACTICE_TIME_MINUTES = "practice_time_minutes"
        private const val PREF_KEY_DISPLAY_THEME = "display_theme"
    }
}