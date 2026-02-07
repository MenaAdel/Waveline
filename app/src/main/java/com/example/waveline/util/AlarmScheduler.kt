package com.example.waveline.util

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.waveline.data.remote.NotificationDto
import com.example.waveline.receiver.NotificationReceiver

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(item: NotificationDto) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("TITLE", item.title)
            putExtra("ID", item.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, item.id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val triggerAt = System.currentTimeMillis() + (item.timeInSeconds * 1000)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
    }

    fun cancelAll() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun cancel(notificationId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}