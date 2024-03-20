package com.terracode.blueharvest.utils


data class Notification(
    val type: NotificationTypes,
    val message: String,
    val timestamp: String
)
