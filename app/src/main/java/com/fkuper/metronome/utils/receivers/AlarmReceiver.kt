package com.fkuper.metronome.utils.receivers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import com.fkuper.metronome.MainActivity
import com.fkuper.metronome.MetronomeApplication
import com.fkuper.metronome.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            Log.w(TAG, "Could not send practice reminder notification: context was null")
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            MetronomeApplication.PRACTICE_REMINDER_NOTIFICATION_ID,
            getNotification(context)
        )
    }

    private fun getNotification(context: Context): Notification {
        val pendingIntent = Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        return NotificationCompat
            .Builder(context, MetronomeApplication.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(getString(context, R.string.practice_reminder_notification_title))
            .setContentText(getString(context, R.string.practice_reminder_notification_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        private const val TAG = "AlarmReceiver"
    }
}