package com.fkuper.metronome.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.fkuper.metronome.MetronomeApplication
import com.fkuper.metronome.data.SharedPreferencesRepository
import com.fkuper.metronome.utils.model.toCalendarDay
import com.fkuper.metronome.utils.receivers.AlarmReceiver
import java.util.Calendar

class PracticeNotificationsAlarmHandler private constructor(private val context: Context) {
    private val prefsRepository: SharedPreferencesRepository
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var alarmIntents: ArrayList<PendingIntent> = arrayListOf()

    init {
        val app = context.applicationContext as MetronomeApplication
        prefsRepository = app.container.preferencesRepository
    }

    fun setupPracticeNotifications() {
        cancelPendingNotifications()

        val practiceRemindersOn = prefsRepository.getPracticeRemindersOn()
        if (!practiceRemindersOn) return

        val practiceDays = prefsRepository.getPracticeDays()
        val practiceHours = prefsRepository.getPracticeHours()

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, practiceHours.hours)
            set(Calendar.MINUTE, practiceHours.minutes)
        }

        practiceDays?.let { days ->
            days.forEach { day ->
                calendar.set(Calendar.DAY_OF_WEEK, day.toCalendarDay())
                val alarmIntent = Intent(context.applicationContext, AlarmReceiver::class.java).let {
                    PendingIntent.getBroadcast(
                        context.applicationContext,
                        System.nanoTime().toInt(),
                        it,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                    )
                }

                if (System.currentTimeMillis() > calendar.timeInMillis) {
                    // if alarm is in the past, instead schedule first time 7 days from that point
                    // this avoids firing the alarm immediately
                    calendar.timeInMillis = calendar.timeInMillis + (AlarmManager.INTERVAL_DAY * 7)
                }

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    alarmIntent!!
                )
                alarmIntents.add(alarmIntent)
            }
        }
    }

    private fun cancelPendingNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        } else {
            alarmIntents.forEach { alarmManager.cancel(it) }
        }
        alarmIntents.clear()
    }

    companion object {
        // We can ignore this since we use application context in the constructor
        // https://stackoverflow.com/questions/37709918/warning-do-not-place-android-context-classes-in-static-fields-this-is-a-memory
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: PracticeNotificationsAlarmHandler? = null

        fun getInstance(context: Context): PracticeNotificationsAlarmHandler =
            instance ?: synchronized(this) {
                instance ?: PracticeNotificationsAlarmHandler(context.applicationContext)
            }
    }

}