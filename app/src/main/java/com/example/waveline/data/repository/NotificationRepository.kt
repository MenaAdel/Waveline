package com.example.waveline.data.repository

import com.example.waveline.data.remote.NotificationApi
import com.example.waveline.data.remote.NotificationDto
import com.example.waveline.util.AlarmScheduler
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val api: NotificationApi,
    private val scheduler: AlarmScheduler
) {
    suspend fun fetchAndSchedule(): List<NotificationDto> {
        val response = api.getNotifications().notifications.notification
        response.forEach { scheduler.schedule(it) }
        return response
    }

    fun cancelAll() = scheduler.cancelAll()
}