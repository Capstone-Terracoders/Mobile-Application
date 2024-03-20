package com.terracode.blueharvest.services.accessibilityValueServices

import android.widget.ArrayAdapter
import android.widget.Spinner
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.listeners.accessibilityListeners.ColorSchemeListener
import com.terracode.blueharvest.utils.PreferenceManager

object ColorSchemeService {
    fun setup(colorSpinner: Spinner, activity: AccessibilitySettingsActivity) {
        PreferenceManager.init(activity)

        val colorSchemeListener = ColorSchemeListener(activity)

        val currentColorPosition = PreferenceManager.getSelectedColorPosition()

        val colors = activity.resources.getStringArray(R.array.colorSchemeArray)
        val colorAdapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item, colors
        )
        colorSpinner.adapter = colorAdapter

        colorSpinner.setSelection(currentColorPosition)

        colorSpinner.onItemSelectedListener = colorSchemeListener
    }
}