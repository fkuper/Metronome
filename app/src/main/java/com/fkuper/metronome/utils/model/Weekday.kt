package com.fkuper.metronome.utils.model

import java.util.Calendar

enum class Weekday(val shortName: String) {
    MONDAY("M"),
    TUESDAY("T"),
    WEDNESDAY("W"),
    THURSDAY("T"),
    FRIDAY("F"),
    SATURDAY("S"),
    SUNDAY("S")
}

fun Weekday.toCalendarDay(): Int {
    return when (this) {
        Weekday.MONDAY -> Calendar.MONDAY
        Weekday.TUESDAY -> Calendar.TUESDAY
        Weekday.WEDNESDAY -> Calendar.WEDNESDAY
        Weekday.THURSDAY -> Calendar.THURSDAY
        Weekday.FRIDAY -> Calendar.FRIDAY
        Weekday.SATURDAY -> Calendar.SATURDAY
        Weekday.SUNDAY -> Calendar.SUNDAY
    }
}