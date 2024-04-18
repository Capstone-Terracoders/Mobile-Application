package com.terracode.blueharvest.services.optimalRangeValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.optimalRangeValueListeners.WheelRadiusListener
import com.terracode.blueharvest.utils.PreferenceManager

object WheelRadiusService {
    fun setup(wheelRadiusInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val wheelRadiusListener = WheelRadiusListener(activity, wheelRadiusInput)
        val wheelRadius: Float = PreferenceManager.getWheelRadiusInput()

        wheelRadius.let {
            // Convert the Float value to String since setText() expects a String
            wheelRadiusInput.setText(it.toString())
        }

        wheelRadiusInput.addTextChangedListener(wheelRadiusListener)
    }
}