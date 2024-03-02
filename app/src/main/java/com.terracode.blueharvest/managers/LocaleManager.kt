package com.terracode.blueharvest.managers

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

@Suppress("DEPRECATION")
object LocaleManager {
    fun setLocale(activity: Activity, languageCode: String) {
        Log.d("CAT", languageCode)
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = activity.resources
        val configuration = Configuration(resources.configuration)
        // Set the new locale configuration
        configuration.setLocale(locale)

        // Update the base context of the application
        activity.baseContext.resources.updateConfiguration(
            configuration,
            activity.baseContext.resources.displayMetrics
        )
    }

    fun getLanguageCode(position: Int): String {
        return when (position) {
            0 -> "en" // English
            1 -> "fr" // French
            2 -> "es" // Spanish
            else -> "en" // Default to English if position is out of range
        }
    }
}