package com.terracode.blueharvest.services.safetyValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.safetyValueListeners.MaxRakeRPMListener
import com.terracode.blueharvest.utils.PreferenceManager

object MaxRakeRPMService {
    fun setup(maxRakeRPMInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val maxRakeRPMListener = MaxRakeRPMListener(activity)
        val maxRakeRPM: Int = PreferenceManager.getMaxRakeRPMInput()

        maxRakeRPM.let {
            // Convert the Int value to String since setText() expects a String
            maxRakeRPMInput.setText(it.toString())
        }

        maxRakeRPMInput.addTextChangedListener(maxRakeRPMListener)
    }
}