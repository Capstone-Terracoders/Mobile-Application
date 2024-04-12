package com.terracode.blueharvest.listeners.accessibilityListeners

import android.widget.CompoundButton
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.UnitConverter

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
        // Set SharedPreferences for this activity
        PreferenceManager.init(activity)

        // Update the value of unit toggle switch in SharedPreferences
        PreferenceManager.setSelectedUnit(isChecked)

        //Convert all data to correct unit here
        var rakeHeight = PreferenceManager.getRakeHeight()
        var maxHeightDisplayed = PreferenceManager.getMaxHeightDisplayedInput()
        var minRakeHeight = PreferenceManager.getMinRakeHeightInput()
        var optimalHeightRange = PreferenceManager.getOptimalHeightRangeInput()

        if (isChecked){
            rakeHeight = UnitConverter.convertHeightToMetric(rakeHeight)!!
            maxHeightDisplayed = UnitConverter.convertHeightToMetric(maxHeightDisplayed)!!
            minRakeHeight = UnitConverter.convertHeightToMetric(minRakeHeight)!!
            optimalHeightRange = UnitConverter.convertHeightToMetric(optimalHeightRange)!!
        } else {
            rakeHeight = UnitConverter.convertHeightToImperial(rakeHeight)!!
            maxHeightDisplayed = UnitConverter.convertHeightToImperial(maxHeightDisplayed)!!
            minRakeHeight = UnitConverter.convertHeightToImperial(minRakeHeight)!!
            optimalHeightRange = UnitConverter.convertHeightToMetric(optimalHeightRange)!!
        }

        PreferenceManager.setCurrentHeight(rakeHeight)
        PreferenceManager.setMaxHeightDisplayedInput(maxHeightDisplayed)
        PreferenceManager.setMinRakeHeightInput(minRakeHeight)
        PreferenceManager.setOptimalHeightRangeInput(optimalHeightRange)
    }
}
