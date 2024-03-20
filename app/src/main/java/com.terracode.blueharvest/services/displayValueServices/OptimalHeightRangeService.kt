package com.terracode.blueharvest.services.displayValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.displayValueListeners.OptimalHeightRangeListener
import com.terracode.blueharvest.utils.PreferenceManager

object OptimalHeightRangeService {
    fun setup(optimalHeightRangeInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val optimalHeightRangeListener = OptimalHeightRangeListener(activity)
        val optimalHeightRange: Int = PreferenceManager.getOptimalHeightRangeInput()

        optimalHeightRange.let {
            // Convert the Int value to String since setText() expects a String
            optimalHeightRangeInput.setText(it.toString())
        }

        optimalHeightRangeInput.addTextChangedListener(optimalHeightRangeListener)
    }
}