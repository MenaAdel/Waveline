package com.example.waveline.data.remote

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
@XmlSerialName("notification", "", "")
data class NotificationDto(
    @XmlElement(true) val id: Int,
    @XmlElement(true) val title: String,
    @XmlElement(true) val timeInSeconds: Long
)

@Serializable
@XmlSerialName("notifications", "", "")
data class NotificationListDto(
    val notifications: List<NotificationDto>
)

@Serializable
@XmlSerialName("xml", "", "")
data class XmlRoot(
    val notifications: NotificationListDto
)