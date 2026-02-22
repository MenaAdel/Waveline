package com.example.waveline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.waveline.ui.details.DetailActivity
import com.example.waveline.ui.notification_list.NotificationScreen
import com.example.waveline.ui.notification_list.NotificationViewModel
import com.example.waveline.ui.theme.WavelineTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: NotificationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationScreen(
                viewModel = viewModel,
                onNavigate = { clickedNotification ->
                    val intent = Intent(this, DetailActivity::class.java).apply {
                        // This works now because we added @Parcelize to the DTO earlier
                        val jsonString = Json.encodeToString(clickedNotification)
                        putExtra("DATA_JSON", jsonString)
                    }
                    startActivity(intent)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
}