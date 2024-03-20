package com.terracode.blueharvest.services.accessibilityValueServices

import androidx.appcompat.widget.SwitchCompat
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.listeners.accessibilityListeners.UnitToggleListener
import com.terracode.blueharvest.utils.PreferenceManager

object UnitService {
    fun setup(unitSwitch: SwitchCompat, activity: AccessibilitySettingsActivity) {
        PreferenceManager.init(activity)

        val unitToggleListener = UnitToggleListener(activity)

        val toggleValue = PreferenceManager.getSelectedUnit()
        unitSwitch.isChecked = toggleValue

        //Logic to change value of the unitToggleValue in the shared preferences when the unit toggle is switched.
        unitSwitch.setOnCheckedChangeListener(unitToggleListener)
    }
}