package com.terracode.blueharvest.listeners.homeListeners

import android.view.View
import android.view.View.OnClickListener
import com.terracode.blueharvest.HomeActivity
import com.terracode.blueharvest.utils.NotificationTypes
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.Notification
import java.time.Instant
import java.time.format.DateTimeFormatter

class RecordButtonListener(private val activity: HomeActivity) : OnClickListener {

    override fun onClick(view: View?) {

        val startRecordingNotification = Notification(
            NotificationTypes.NOTIFICATION,
            "Started Recording Data",
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )

        val stopRecordingNotification = Notification(
            NotificationTypes.NOTIFICATION,
            "Stopped Recording Data",
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )

        // Get the current status from SharedPreferences
        val currentStatus = PreferenceManager.getRecordButtonStatus()

        // Toggle the status
        val newStatus = !currentStatus

        // Update the status in SharedPreferences
        PreferenceManager.setRecordButtonStatus(newStatus)

        // Optionally, you can perform additional actions based on the new status
        if (newStatus) {
            // If the status is true, record and send notification
            PreferenceManager.setNotification(startRecordingNotification)
        } else {
            // If the status is false, stop recording and send notification
            PreferenceManager.setNotification(stopRecordingNotification)
        }
    }
}
