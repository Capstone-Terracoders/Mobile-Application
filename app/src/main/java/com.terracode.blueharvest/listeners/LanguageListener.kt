package com.terracode.blueharvest.listeners

import android.view.View
import android.widget.AdapterView
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.utils.viewManagers.LocaleManager

/**
 * Listener for handling language selection changes in the accessibility settings screen.
 * This listener is triggered when the user selects a different language from the options.
 *
 * @author MacKenzie Young 3/2/2024
 *
 * @param activity The parent AccessibilitySettingsActivity instance.
 */
class LanguageSelectionListener(private val activity: AccessibilitySettingsActivity) :
    AdapterView.OnItemSelectedListener {

    /**
     * Called when an item in the language selection spinner is selected.
     *
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that is selected.
     */
    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View?,
        position: Int,
        id: Long
    ) {
        // Set SharedPreferences for this activity
        PreferenceManager.init(activity)

        // Retrieve the position of the currently selected language
        val currentLanguagePosition = PreferenceManager.getSelectedLanguagePosition()

        // Get the language code of the selected language position
        val selectedLanguageCode = LocaleManager.getLanguageCode(position)

        // Check if the selected position is different from the current position
        if (currentLanguagePosition != position) {
            // Set the locale to the selected language
            LocaleManager.setLocale(activity, selectedLanguageCode)

            // Update the selected language position in PreferenceManager
            PreferenceManager.setSelectedLanguagePosition(position)

            // Recreate the activity to apply the changes
            activity.recreate()
        }
    }

    /**
     * Called when nothing is selected in the language selection spinner.
     *
     * @param parent The AdapterView where the selection happened.
     */
    override fun onNothingSelected(parent: AdapterView<*>) {
        // No action needed when nothing is selected
    }
}
