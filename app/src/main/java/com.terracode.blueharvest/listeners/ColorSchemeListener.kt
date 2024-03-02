package com.terracode.blueharvest.listeners

import android.view.View
import android.widget.AdapterView
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.utils.ThemeHelper

class ColorSchemeListener(private val activity: AccessibilitySettingsActivity) :
    AdapterView.OnItemSelectedListener {

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val currentColorPosition = sharedPreferences.getInt("selectedColorPosition", 0)

        if (position != currentColorPosition) {
            val selectedColorTheme = ThemeHelper.getThemeResource(position)
            ThemeHelper.setColorOverlayTheme(activity, selectedColorTheme)
            sharedPreferences.edit().putInt("selectedColorPosition", position).apply()
            activity.recreate()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}