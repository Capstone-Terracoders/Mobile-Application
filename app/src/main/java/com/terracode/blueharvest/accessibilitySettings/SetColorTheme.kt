package com.terracode.blueharvest.accessibilitySettings

import android.content.Context
import android.content.SharedPreferences
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.R
@Suppress("DEPRECATION")
class SetColorTheme(private val context: Context) {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var colorSpinner: Spinner

    init {
        initializeColorThemeSettings()
    }

    private fun initializeColorThemeSettings() {
        //Initialized the shared preferences and sets the XML components equal to the id in the XML file.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        //-----Logic for Color Scheme-----//
        val currentColorPosition = sharedPreferences.getInt("selectedColorPosition", 0)

        val colors = context.resources.getStringArray(R.array.colorSchemeArray)
        colorSpinner = Spinner(context)
        val colorAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, colors
        )
        colorSpinner.adapter = colorAdapter

        colorSpinner.setSelection(currentColorPosition)

        colorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?, position: Int, id: Long
            ) {
                val selectedColorTheme = getColorTheme(position)
                if (currentColorPosition != position) {
                    //setTheme(selectedColorTheme)
                    sharedPreferences.edit().putInt("selectedColorPosition", position).apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun getColorTheme(position: Int): String {
        return when (position) {
            0 -> "Theme.Light" // Light Mode
            1 -> "Theme.Dark" // Dark Mode
            2 -> "Theme.Colorblind" // Colorblind Mode
            else -> "Theme.Light" // Default to light mode if position is out of range
        }
    }
}
