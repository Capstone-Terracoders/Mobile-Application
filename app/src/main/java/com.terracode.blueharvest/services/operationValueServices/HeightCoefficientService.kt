package com.terracode.blueharvest.services.operationValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.operationValueListeners.HeightCoefficientListener
import com.terracode.blueharvest.utils.PreferenceManager

object HeightCoefficientService {
    fun setup(heightCoefficientInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val heightCoefficientListener = HeightCoefficientListener(activity, heightCoefficientInput)
        val heightCoefficient: Float = PreferenceManager.getHeightCoefficientInput()

        heightCoefficient.let {
            // Convert the Int value to String since setText() expects a String
            heightCoefficientInput.setText(it.toString())
        }

        heightCoefficientInput.addTextChangedListener(heightCoefficientListener)
    }
}