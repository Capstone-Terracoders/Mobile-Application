package com.terracode.blueharvest.utils

import com.terracode.blueharvest.utils.constants.Notification
import com.terracode.blueharvest.utils.constants.NotificationTypes
import java.time.Instant
import java.time.format.DateTimeFormatter

object Notifications {

    fun getMaxRPMReachedNotification(): Notification {
        // Create a new warning notification for currentRpm > maxRPM
        return Notification(
            NotificationTypes.WARNING,
            "Current RPM is greater than maximum RPM!",
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxHeightReachedNotification(): Notification {
        // Create a new warning notification for currentRpm > maxRPM
        return Notification(
            NotificationTypes.WARNING,
            "Current rake height is greater than maximum height!",
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getStartRecordingNotification(): Notification {
        // Create a new notification for start recording data
        return Notification(
            NotificationTypes.NOTIFICATION,
            "Started Recording Data",
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getStopRecordingNotification(): Notification {
        // Create a new notification for stopping recording data
        return Notification(
            NotificationTypes.NOTIFICATION,
            "Stopped Recording Data",
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }
}
