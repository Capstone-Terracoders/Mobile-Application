package com.terracode.blueharvest.listeners

import android.widget.CompoundButton
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity

class UnitToggleListener(private val activity: AccessibilitySettingsActivity) :
    CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity) // Access preferences within activity context
        sharedPreferences.edit().putBoolean("unitToggleValue", isChecked).apply()
    }
}