package com.example.waveline.ui.notification_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.waveline.data.remote.NotificationDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    onNavigate: (NotificationDto) -> Unit
) {
    val items by viewModel.uiState.collectAsState()
    val loading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                actions = {
                    TextButton(onClick = { viewModel.cancelAll() }) {
                        Text("CANCEL ALL", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(modifier = Modifier.padding(padding)) {
            items(items) { item ->
                ListItem(
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text("Delay: ${item.timeInSeconds}s") },
                    modifier = Modifier.clickable {
                        onNavigate(item)
                    }
                )
            }
        }
    }
}