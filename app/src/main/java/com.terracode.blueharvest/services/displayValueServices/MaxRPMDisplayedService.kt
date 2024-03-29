package com.terracode.blueharvest.services.displayValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.displayValueListeners.MaxRPMDisplayListener
import com.terracode.blueharvest.utils.PreferenceManager

object MaxRPMDisplayedService {
    fun setup(maxRPMDisplayedInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val maxRPMDisplayedListener = MaxRPMDisplayListener(activity, maxRPMDisplayedInput)
        val maxRPMDisplayed: Int = PreferenceManager.getMaxRPMDisplayedInput()

        maxRPMDisplayed.let {
            // Convert the Int value to String since setText() expects a String
            maxRPMDisplayedInput.setText(it.toString())
        }

        maxRPMDisplayedInput.addTextChangedListener(maxRPMDisplayedListener)
    }
}