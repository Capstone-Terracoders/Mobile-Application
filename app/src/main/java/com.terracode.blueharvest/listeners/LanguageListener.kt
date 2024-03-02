package com.terracode.blueharvest.listeners

import android.content.res.Configuration
import android.view.View
import android.widget.AdapterView
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity
import java.util.Locale

@Suppress("DEPRECATION")
class LanguageSelectionListener(private val activity: AccessibilitySettingsActivity) :
    AdapterView.OnItemSelectedListener {

    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View?, position: Int, id: Long
    ) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity) // Access preferences within activity context
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        val selectedLanguageCode = getLanguageCode(position)
        if (currentLanguagePosition != position) {
            setLocale(selectedLanguageCode)
            sharedPreferences.edit().putInt("selectedLanguagePosition", position).apply()
            activity.recreate()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // write code to perform some action
    }

    fun getLanguageCode(position: Int): String {
        return when (position) {
            0 -> "en" // English
            1 -> "fr" // French
            2 -> "es" // Spanish
            else -> "en" // Default to English if position is out of range
        }
    }

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = activity.resources
        val configuration = Configuration(resources.configuration)
        // Set the new locale configuration
        configuration.setLocale(locale)

        // Update the base context of the application
        activity.baseContext.resources.updateConfiguration(configuration, activity.baseContext.resources.displayMetrics)
    }
}