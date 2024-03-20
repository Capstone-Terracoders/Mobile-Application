package com.terracode.blueharvest.services.safetyValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.safetyValueListeners.MinRakeHeightListener
import com.terracode.blueharvest.utils.PreferenceManager

object MinRakeHeightService {
    fun setup(minRakeHeightInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val minRakeHeightListener = MinRakeHeightListener(activity)
        val maxRakeRPM: Int = PreferenceManager.getMinRakeHeightInput()

        maxRakeRPM.let {
            // Convert the Int value to String since setText() expects a String
            minRakeHeightInput.setText(it.toString())
        }

        minRakeHeightInput.addTextChangedListener(minRakeHeightListener)
    }
}