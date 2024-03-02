package com.terracode.blueharvest.listeners

import android.view.View
import android.widget.AdapterView
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.utils.LocaleHelper


class LanguageSelectionListener(private val activity: AccessibilitySettingsActivity) :
    AdapterView.OnItemSelectedListener {

    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View?,
        position: Int,
        id: Long
    ) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity) // Access preferences within activity context
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        val selectedLanguageCode = LocaleHelper.getLanguageCode(position)
        if (currentLanguagePosition != position) {
            LocaleHelper.setLocale(activity,selectedLanguageCode)
            sharedPreferences.edit().putInt("selectedLanguagePosition", position).apply()
            activity.recreate()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // write code to perform some action
    }
}