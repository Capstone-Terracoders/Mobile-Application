package com.terracode.blueharvest.services.accessibilityValueServices

import android.widget.ArrayAdapter
import android.widget.Spinner
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.listeners.accessibilityListeners.LanguageSelectionListener
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.LocaleManager

object LanguageService {
    fun setup(languageSpinner: Spinner, activity: AccessibilitySettingsActivity) {
        PreferenceManager.init(activity)

        val currentLanguagePosition = PreferenceManager.getSelectedLanguagePosition()
        val languagePosition = LocaleManager.getLanguageCode(currentLanguagePosition)
        val languageListener = LanguageSelectionListener(activity)

        LocaleManager.setLocale(activity, languagePosition)

        val languages = activity.resources.getStringArray(R.array.languageArray)
        val languageAdapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item, languages
        )
        languageSpinner.adapter = languageAdapter

        languageSpinner.setSelection(currentLanguagePosition)

        languageSpinner.onItemSelectedListener = languageListener
    }
}