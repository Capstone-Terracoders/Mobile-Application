package com.terracode.blueharvest.listeners.safetyValueListeners

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.utils.PreferenceManager

class MaxRakeRPMListener(private val activity: ConfigurationSettingsActivity) :
    TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        PreferenceManager.init(activity)
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No action needed during text change
    }

    override fun afterTextChanged(editable: Editable?) {
        editable?.let { it ->
            val input = it.toString()
            if (input.isNotEmpty()) {
                val value = input.toIntOrNull()
                value?.let {
                    if (it > 10000){
                        Toast.makeText(activity, "Rake RPM should not exceed 10,000", Toast.LENGTH_SHORT).show()
                    } else {
                        PreferenceManager.setMaxRakeRPMInput(it)
                    }
                }
            }
        }
    }
}
