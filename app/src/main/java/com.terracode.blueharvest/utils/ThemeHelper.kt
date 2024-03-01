package com.terracode.blueharvest.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.R

object ThemeHelper {

    // Function to get the current theme based on the theme position
    fun getCurrentTheme(context: Context): Int {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val currentThemePosition = sharedPreferences.getInt("selectedColorPosition", 0)
        return getThemeResourceId(currentThemePosition)
    }
    fun getThemeResourceId(themePosition: Int): Int {
        return when (themePosition) {
            0 -> R.style.DynamicColors_Overlay_Light
            1 -> R.style.DynamicColors_Overlay_Dark
            2 -> R.style.DynamicColors_Overlay_Colorblind
            else -> R.style.DynamicColors_Overlay_Light // Default to light mode if themeName is unknown
        }
    }
}

