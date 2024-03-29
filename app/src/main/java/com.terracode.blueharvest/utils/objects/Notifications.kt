package com.terracode.blueharvest.utils.objects

import android.content.Context
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.constants.NotificationTypes
import java.time.Instant
import java.time.format.DateTimeFormatter

object Notifications {

    fun getMaxRPMDisplayedReachedNotification(context: Context): Notification {
        val notificationMessage = R.string.maxRPMDisplayedReachedNotification
        return Notification(
            NotificationTypes.WARNING,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxRPMReachedNotification(context: Context): Notification {
        val notificationMessage = R.string.maxRPMReachedNotification
        return Notification(
            NotificationTypes.WARNING,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getRpmBelowZeroNotification(context: Context): Notification {
        val notificationMessage = R.string.rpmBelowZeroNotification
        return Notification(
            NotificationTypes.ERROR,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxHeightReachedNotification(context: Context): Notification {
        val notificationMessage = R.string.maxHeightReachedNotification
        return Notification(
            NotificationTypes.WARNING,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMinHeightReachedNotification(context: Context): Notification {
        val notificationMessage = R.string.minHeightReachedNotification
        return Notification(
            NotificationTypes.WARNING,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getHeightBelowZeroNotification(context: Context): Notification {
        val notificationMessage = R.string.heightBelowZeroNotification
        return Notification(
            NotificationTypes.ERROR,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getStartRecordingNotification(context: Context): Notification {
        val notificationMessage = R.string.startedRecordingNotification
        return Notification(
            NotificationTypes.NOTIFICATION,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getStopRecordingNotification(context: Context): Notification {
        val notificationMessage = R.string.stoppedRecordingNotification
        return Notification(
            NotificationTypes.NOTIFICATION,
            context.getString(notificationMessage),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxInputNotification(inputName: String, value: Int): Notification {
        val notificationMessage = "Input: $value for $inputName not saved. \nPlease enter a number smaller than 10,000."
        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxInputFloatNotification(inputName: String, value: Float): Notification {
        val notificationMessage = "Input: $value for $inputName not saved. \nPlease enter a number smaller than 10,000."
        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun safetyValueGreaterThanDisplayValueNotification(inputName: String, value: Int): Notification {
        val notificationMessage = "WARNING: Input: $value for $inputName is greater than Display Value."
        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun rangeOutOfBoundsNotification(inputName: String, value: Float): Notification {
        val notificationMessage = "WARNING: Input: $value for $inputName makes optimal values out of bounds of viewing area."
        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }
}
