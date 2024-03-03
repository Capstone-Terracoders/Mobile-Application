package com.terracode.blueharvest.viewManagers

import android.app.Activity
import android.content.res.Configuration
import java.util.Locale

/**
 * Singleton object for managing locale changes within the application.
 *
 * @author MacKenzie Young 3/2/2024
 *
 */
@Suppress("DEPRECATION")
object LocaleManager {
    /**
     * Set the locale of the application to the specified language.
     *
     * @param activity The current activity.
     * @param languageCode The language code representing the desired locale.
     */
    fun setLocale(activity: Activity, languageCode: String) {
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

    /**
     * Get the language code corresponding to the selected position in the language spinner.
     *
     * @param position The position of the selected language.
     * @return The language code corresponding to the selected position.
     */
    fun getLanguageCode(position: Int): String {
        return when (position) {
            0 -> "en" // English
            1 -> "fr" // French
            2 -> "es" // Spanish
            else -> "en" // Default to English if position is out of range
        }
    }
}
