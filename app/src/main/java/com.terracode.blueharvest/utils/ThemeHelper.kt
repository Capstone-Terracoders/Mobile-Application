package com.terracode.blueharvest.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.R

/**
 * Utility object for managing themes in the application.
 * This object provides functions to retrieve the current theme and theme resources.
 */
object ThemeHelper {

    /**
     * Retrieves the current theme based on the stored theme position in SharedPreferences.
     *
     * @param context The context used to access SharedPreferences.
     * @return The resource ID of the current theme.
     */
    fun getCurrentTheme(context: Context): Int {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val currentThemePosition = sharedPreferences.getInt("selectedColorPosition", 0)
        return getThemeResource(currentThemePosition)
    }

    /**
     * Retrieves the resource ID of a theme based on its position.
     *
     * @param themePosition The position of the theme.
     * @return The resource ID of the corresponding theme.
     */
    fun getThemeResource(themePosition: Int): Int {
        return when (themePosition) {
            0 -> R.style.DynamicColors_Overlay_Light // Light theme
            1 -> R.style.DynamicColors_Overlay_Dark // Dark theme
            2 -> R.style.DynamicColors_Overlay_Colorblind // Colorblind theme
            else -> R.style.DynamicColors_Overlay_Light // Default to light mode if themeName is unknown
        }
    }
}


