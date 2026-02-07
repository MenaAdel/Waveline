package com.example.waveline.ui.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.waveline.data.remote.NotificationDto
import com.example.waveline.util.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject
import androidx.appcompat.app.AlertDialog

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var scheduler: AlarmScheduler
    private var notification: NotificationDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the data passed from Activity A
        val jsonString = intent.getStringExtra("DATA_JSON")
        notification = jsonString?.let { Json.decodeFromString<NotificationDto>(it) }

        setContent {
            DetailScreen(
                title = notification?.title ?: "Unknown",
                onScheduleClick = { scheduleNotification() }
            )
        }
    }

    private fun scheduleNotification() {
        notification?.let {
            scheduler.schedule(it)
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("Notification '${notification?.title}' has been scheduled!")
            .setPositiveButton("OK", null)
            .show()
    }

    // Action Bar Setup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val cancelButton = menu?.add(0, 1, 0, "Cancel")
        cancelButton?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            notification?.let {
                scheduler.cancel(it.id)
                Toast.makeText(this, "Notification Canceled", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}