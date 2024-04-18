package com.terracode.blueharvest.services.optimalRangeValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.optimalRangeValueListeners.RakeRadiusListener
import com.terracode.blueharvest.utils.PreferenceManager

object RakeRadiusService {
    fun setup(rakeRadiusInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val rakeRadiusListener = RakeRadiusListener(activity, rakeRadiusInput)
        val rakeRadius: Float = PreferenceManager.getRakeRadiusInput()

        rakeRadius.let {
            // Convert the Float value to String since setText() expects a String
            rakeRadiusInput.setText(it.toString())
        }

        rakeRadiusInput.addTextChangedListener(rakeRadiusListener)
    }
}