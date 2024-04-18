package com.terracode.blueharvest.services.displayValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.displayValueListeners.MaxHeightDisplayListener
import com.terracode.blueharvest.utils.PreferenceManager

object MaxHeightDisplayedService {
    fun setup(maxHeightDisplayedInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val maxHeightDisplayedListener = MaxHeightDisplayListener(activity, maxHeightDisplayedInput)
        val maxHeightDisplayed: Float = PreferenceManager.getMaxHeightDisplayedInput()

        maxHeightDisplayed.let {
            // Convert the Int value to String since setText() expects a String
            maxHeightDisplayedInput.setText(it.toString())
        }

        maxHeightDisplayedInput.addTextChangedListener(maxHeightDisplayedListener)
    }
}