package com.terracode.blueharvest.utils.viewManagers

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.HomeActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.Notification
import com.terracode.blueharvest.utils.NotificationTypes
import com.terracode.blueharvest.utils.PreferenceManager

object NotificationManager {
    @SuppressLint("InflateParams", "SetTextI18n")
    fun showNotificationList(activity: HomeActivity, anchorView: View, notifications: List<Notification>) {
        val popupView = activity.layoutInflater.inflate(R.layout.notification_layout, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        // Find the container layout in the popup view
        val containerLayout = popupView.findViewById<LinearLayout>(R.id.notificationContainer)

        // Find the clearNotificationsButton directly from the inflated view
        val clearNotificationButton = popupView.findViewById<Button>(R.id.clearNotificationsButton)

        // Add notifications dynamically to the container layout
        if (notifications.isEmpty()){
            val textView = TextView(activity)
            textView.text = "No Notifications Yet!"
            containerLayout.addView(textView)
        } else {
            notifications.forEach { notification ->
                val textView = TextView(activity)
                //If we like the timestamp on a new line keep \n if we want it on one line do \t
                textView.text = "${notification.message} \n ${notification.timestamp}"

                // Set icon based on notification type
                val icon = when (notification.type) {
                    NotificationTypes.WARNING -> {
                        val drawable =
                            ContextCompat.getDrawable(activity, R.drawable.exclamation_triangle_fill)
                        drawable?.setTint(Color.YELLOW)
                        drawable
                    }

                    NotificationTypes.ERROR -> {
                        val drawable =
                            ContextCompat.getDrawable(activity, R.drawable.exclamation_circle_fill)
                        drawable?.setTint(Color.RED)
                        drawable
                    }

                    NotificationTypes.NOTIFICATION -> {
                        val drawable = ContextCompat.getDrawable(activity, R.drawable.info_circle_fill)
                        drawable?.setTint(Color.BLUE)
                        drawable
                    }
                }

                // Set the drawable icon to the left of the text
                icon?.let {
                    textView.setCompoundDrawablesWithIntrinsicBounds(it, null, null, null)
                    val padding = activity.resources.getDimensionPixelSize(R.dimen.icon_padding)
                    textView.compoundDrawablePadding = padding
                }

                containerLayout.addView(textView)
            }
        }

        // Set up Listener for Clearing Notifications
        clearNotificationButton.setOnClickListener {
            // Call the method to clear notifications
            PreferenceManager.clearNotifications()
            // Dismiss the popup window after clearing notifications
            popupWindow.dismiss()
        }

        // Set a dismiss listener to close the popup when clicked outside
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // Show the popup below the anchor view (bell icon)
        popupWindow.showAsDropDown(anchorView)
    }
}