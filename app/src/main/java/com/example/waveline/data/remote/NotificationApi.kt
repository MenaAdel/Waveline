package com.example.waveline.data.remote

import retrofit2.http.GET

interface NotificationApi {
    @GET("localalerts.php")
    suspend fun getNotifications(): NotificationListDto
}