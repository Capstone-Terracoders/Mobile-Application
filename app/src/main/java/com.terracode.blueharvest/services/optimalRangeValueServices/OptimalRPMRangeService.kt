package com.terracode.blueharvest.services.optimalRangeValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.optimalRangeValueListeners.OptimalRPMRangeListener
import com.terracode.blueharvest.utils.PreferenceManager

object OptimalRPMRangeService {
    fun setup(optimalRPMRangeInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val optimalRPMRangeListener = OptimalRPMRangeListener(activity, optimalRPMRangeInput)
        val optimalRPMRange: Float = PreferenceManager.getOptimalRPMRangeInput()

        optimalRPMRange.let {
            // Convert the Float value to String since setText() expects a String
            optimalRPMRangeInput.setText(it.toString())
        }

        optimalRPMRangeInput.addTextChangedListener(optimalRPMRangeListener)
    }
}