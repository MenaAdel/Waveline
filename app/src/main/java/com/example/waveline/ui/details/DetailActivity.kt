package com.example.waveline.ui.details

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.waveline.data.remote.NotificationDto
import com.example.waveline.util.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {

    @Inject
    lateinit var scheduler: AlarmScheduler
    private var notification: NotificationDto? = null

    private val CHANNEL_ID = "ALERTS_CHAN"

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showToast("Notifications enabled")
        } else {
            showToast("Notifications disabled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val jsonString = intent.getStringExtra("DATA_JSON")
        notification = jsonString?.let { Json.decodeFromString<NotificationDto>(it) }

        setContent {
            notification?.let {
                DetailScreen(
                    notification = it,
                    onScheduleClick = { scheduleNotification() },
                    onCancel = { id ->
                        scheduler.cancel(id)
                    }
                )
            }
        }

        checkNotificationPermission()
    }

    /**
     * Creates the notification channel required for Android 8.0+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Waveline Alerts"
            val descriptionText = "Notifications for scheduled wave alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun scheduleNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                showExactAlarmDialog()
                return
            }
        }

        notification?.let {
            showConfirmationDialog()
        }
    }

    private fun showExactAlarmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("To schedule alerts at precise times, Waveline needs the 'Alarms & Reminders' permission.")
            .setPositiveButton("Open Settings") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Scheduling Notification")
            .setMessage("Your notification will be scheduled for ${notification?.timeInSeconds} seconds!")
            .setPositiveButton("OK") { _, _ ->
                scheduler.schedule(notification!!)
                showToast("Notification Scheduled")
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val cancelButton = menu?.add(0, 1, 0, "Cancel")
        cancelButton?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            notification?.let {
                scheduler.cancel(it.id)
                showToast("Notification Canceled")
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

private fun DetailActivity.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}