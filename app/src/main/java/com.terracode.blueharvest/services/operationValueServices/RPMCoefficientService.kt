package com.terracode.blueharvest.services.operationValueServices

import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.listeners.operationValueListeners.RPMCoefficientListener
import com.terracode.blueharvest.utils.PreferenceManager

object RPMCoefficientService {
    fun setup(rpmCoefficientInput: EditText, activity: ConfigurationSettingsActivity) {
        PreferenceManager.init(activity)

        val rpmCoefficientListener = RPMCoefficientListener(activity, rpmCoefficientInput)
        val rpmCoefficient: Float = PreferenceManager.getRPMCoefficientInput()

        rpmCoefficient.let {
            // Convert the Int value to String since setText() expects a String
            rpmCoefficientInput.setText(it.toString())
        }

        rpmCoefficientInput.addTextChangedListener(rpmCoefficientListener)
    }
}