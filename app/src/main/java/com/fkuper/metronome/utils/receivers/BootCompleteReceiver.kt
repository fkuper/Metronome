package com.fkuper.metronome.utils.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fkuper.metronome.utils.PracticeNotificationsAlarmHandler

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            val notificationHandler = PracticeNotificationsAlarmHandler.getInstance(context)
            notificationHandler.setupPracticeNotifications()
        }
    }

}