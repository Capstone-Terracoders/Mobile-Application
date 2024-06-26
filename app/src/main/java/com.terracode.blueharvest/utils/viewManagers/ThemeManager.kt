package com.terracode.blueharvest.utils.viewManagers

import android.app.Activity
import android.content.Context
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager

/**
 * Utility object for managing themes in the application.
 * This object provides functions to retrieve the current theme and theme resources.
 *
 * @author MacKenzie Young 3/2/2024
 *
 */
object ThemeManager {
    /**
     * Retrieves the current theme based on the stored theme position in SharedPreferences.
     *
     * @param context The context used to access SharedPreferences.
     * @return The resource ID of the current theme.
     */
    fun getCurrentTheme(context: Context): Int {
        // Set SharedPreferences for this activity
        PreferenceManager.init(context)
        val currentThemePosition = PreferenceManager.getSelectedColorPosition()
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

    fun setColorOverlayTheme(activity: Activity, colorOverlay: Int) {
        val options = DynamicColorsOptions.Builder().setThemeOverlay(colorOverlay).build()
        DynamicColors.applyToActivityIfAvailable(activity, options)
    }
}


