package com.example.waveline.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.waveline.R
import com.example.waveline.ui.details.DetailActivity

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TITLE") ?: "Alert"
        val id = intent.getIntExtra("ID", 0)

        val activityIntent = Intent(context, DetailActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, id, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "ALERTS_CHAN")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Scheduled Alert")
            .setContentText(title)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(id, notification)
    }
}