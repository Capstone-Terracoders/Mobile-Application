package com.terracode.blueharvest.services.toolbarServices

import android.app.Activity
import android.widget.Button
import com.terracode.blueharvest.listeners.toolbarListeners.BackButtonListener
import com.terracode.blueharvest.utils.PreferenceManager

object BackButtonService {
    fun setup(backButton: Button, activity: Activity) {
        PreferenceManager.init(activity)

        val backButtonListener = BackButtonListener(activity)
        backButton.setOnClickListener(backButtonListener)
    }
}
