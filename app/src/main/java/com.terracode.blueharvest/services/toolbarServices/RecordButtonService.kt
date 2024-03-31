package com.terracode.blueharvest.services.toolbarServices

import android.widget.Button
import com.terracode.blueharvest.HomeActivity
import com.terracode.blueharvest.listeners.toolbarListeners.RecordButtonListener
import com.terracode.blueharvest.utils.PreferenceManager

object RecordButtonService {
    fun setup(recordButton: Button, activity: HomeActivity) {
        PreferenceManager.init(activity)

        val recordButtonListener = RecordButtonListener(activity)
        recordButton.setOnClickListener(recordButtonListener)
    }
}
