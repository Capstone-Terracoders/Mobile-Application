package com.terracode.blueharvest.listeners

import android.widget.CompoundButton
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity

/**
 * Listener for handling changes in unit toggle switch in the accessibility settings screen.
 * This listener is triggered when the user toggles the switch for units (e.g., metric/imperial).
 *
 * @author MacKenzie Young 3/2/2024
 *
 * @param activity The parent AccessibilitySettingsActivity instance.
 */
class UnitToggleListener(private val activity: AccessibilitySettingsActivity) :
    CompoundButton.OnCheckedChangeListener {

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked The new checked state of buttonView.
     */
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        // Retrieve SharedPreferences for managing application preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        // Update the value of unit toggle switch in SharedPreferences
        sharedPreferences.edit().putBoolean("unitToggleValue", isChecked).apply()
    }
}
