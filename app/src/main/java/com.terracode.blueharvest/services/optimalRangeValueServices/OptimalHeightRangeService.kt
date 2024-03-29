package com.terracode.blueharvest.services.optimalRangeValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.optimalRangeValueListeners.OptimalHeightRangeListener
import com.terracode.blueharvest.utils.PreferenceManager

object OptimalHeightRangeService {
    fun setup(optimalHeightRangeInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val optimalHeightRangeListener = OptimalHeightRangeListener(activity)
        val optimalHeightRange: Float = PreferenceManager.getOptimalHeightRangeInput()

        optimalHeightRange.let {
            // Convert the Float value to String since setText() expects a String
            optimalHeightRangeInput.setText(it.toString())
        }

        optimalHeightRangeInput.addTextChangedListener(optimalHeightRangeListener)
    }
}