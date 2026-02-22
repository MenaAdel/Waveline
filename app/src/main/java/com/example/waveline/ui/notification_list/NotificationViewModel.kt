package com.example.waveline.ui.notification_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waveline.data.remote.NotificationDto
import com.example.waveline.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<List<NotificationDto>>(emptyList())
    val uiState = _uiState.asStateFlow()

    val isLoading = MutableStateFlow(false)

    fun loadData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repository.fetchAndSchedule()
                _uiState.value = response
            } catch (e: Exception) {
                Log.e("error" ,e.message.toString())
            }
            isLoading.value = false
        }
    }

    fun cancelAll() = repository.cancelAllScheduled(_uiState.value)
}