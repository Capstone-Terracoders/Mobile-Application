package com.terracode.blueharvest.listeners.accessibilityListeners

import android.view.View
import android.widget.AdapterView
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.utils.viewManagers.ThemeManager
import com.terracode.blueharvest.utils.PreferenceManager

/**
 * Listener for handling color scheme changes.
 * This listener is triggered when the user selects a different color scheme from the options
 * in the accessibility settings screen.
 *
 * @author MacKenzie Young 3/2/2024
 *
 * @param activity The parent AccessibilitySettingsActivity instance.
 */
class ColorSchemeListener(private val activity: AccessibilitySettingsActivity) :
    AdapterView.OnItemSelectedListener {

    /**
     * Called when an item in the color scheme selection spinner is selected.
     *
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that is selected.
     */
    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        // Set SharedPreferences for this activity
        PreferenceManager.init(activity)

        // Retrieve the position of the currently selected color scheme
        val currentColorPosition = PreferenceManager.getSelectedColorPosition()

        // Check if the selected position is different from the current position
        if (position != currentColorPosition) {
            // Get the selected color theme resource
            val selectedColorTheme = ThemeManager.getThemeResource(position)

            // Apply the selected color overlay theme from the viewManager package
            ThemeManager.setColorOverlayTheme(activity, selectedColorTheme)

            // Update the selected color position in PreferenceManager
            PreferenceManager.setSelectedColorPosition(position)

            // Recreate the activity to apply the changes
            activity.recreate()
        }
    }

    /**
     * Called when nothing is selected in the color scheme selection spinner.
     *
     * @param parent The AdapterView where the selection happened.
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // No action needed when nothing is selected
    }
}
