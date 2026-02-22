package com.example.waveline.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.waveline.data.remote.NotificationDto

/*
@Composable
fun DetailScreen(title: String, onScheduleClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onScheduleClick) {
                Text("Schedule this notification")
            }
        }
    }
}*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    notification: NotificationDto,
    onScheduleClick: () -> Unit,
    onCancel: (id: Int) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Waveline Test") },

                actions = {
                    TextButton(onClick = { onCancel(notification.id) }) {
                        Text("CANCEL")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A), // Dark grey/black background
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )

            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = notification.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    onScheduleClick()
                },
                modifier = Modifier.fillMaxWidth(0.8f), // Matches visual width in screenshot
                shape = MaterialTheme.shapes.extraSmall // Sharper corners like screenshot
            ) {
                Text("Schedule this notification")
            }
        }
    }
}
