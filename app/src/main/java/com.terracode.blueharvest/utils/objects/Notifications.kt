package com.terracode.blueharvest.utils.objects

import android.content.Context
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.constants.NotificationTypes
import java.time.Instant
import java.time.format.DateTimeFormatter
import com.terracode.blueharvest.utils.constants.MaxUserInputString


object Notifications {
    private val defaultMaxInput = MaxUserInputString.MAX_DEFAULT_INPUT.value
    private val heightMaxInput = MaxUserInputString.MAX_HEIGHT_INPUT.value

    //Current value notifications
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

    //Recording notifications

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

    //Configuration Settings Notifications

    fun getMaxInputDefaultNotification(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.maxInputNotification,
            value,
            inputName,
            defaultMaxInput
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxInputDefaultNotificationFloat(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.maxInputFloatNotification,
            value,
            inputName,
            defaultMaxInput
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxInputHeightNotification(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.maxInputNotification,
            value,
            inputName,
            heightMaxInput
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxInputHeightNotificationFloat(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.maxInputFloatNotification,
            value,
            inputName,
            heightMaxInput
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun safetyValueGreaterThanDisplayValueNotification(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.safetyValueGreaterThanDisplayValueNotification,
            value,
            inputName
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun rangeOutOfBoundsNotification(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.rangeOutOfBoundsNotification,
            value,
            inputName
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun notDivisibleByFiveNotification(context: Context, inputName: String, value: Int): Notification {
        val notificationMessage = context.getString(
            R.string.notDivisibleByFiveNotification,
            value,
            inputName
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun inputBelowFiveNotification(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.inputBelowFiveNotification,
            value,
            inputName
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun displayedValueLessThanSafetyValueNotification(context: Context, inputName: String, value: Float): Notification {
        val notificationMessage = context.getString(
            R.string.displayedValueLessThanSafetyValueNotification,
            value,
            inputName
        )

        return Notification(
            NotificationTypes.WARNING,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    //Custom Toasts for bluetooth
    fun bluetoothDeviceConnectedNotification(context: Context, bluetoothDevice: String): Notification {
        val notificationMessage = context.getString(
            R.string.bluetoothDeviceConnectedNotificationToast,
            bluetoothDevice
        )

        return Notification(
            NotificationTypes.NOTIFICATION,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun bluetoothDeviceDisconnectedNotification(context: Context, bluetoothDevice: String): Notification {
        val notificationMessage = context.getString(
            R.string.bluetoothDeviceDisconnectedNotificationToast,
            bluetoothDevice
        )

        return Notification(
            NotificationTypes.NOTIFICATION,
            notificationMessage,
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }
}
