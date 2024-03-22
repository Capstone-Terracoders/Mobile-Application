package com.terracode.blueharvest.utils.constants

import com.terracode.blueharvest.utils.constants.NotificationTypes


data class Notification(
    val type: NotificationTypes,
    val message: String,
    val timestamp: String
)
