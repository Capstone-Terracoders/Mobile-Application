package com.terracode.blueharvest.accessibilitySettings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.widget.SwitchCompat
import com.terracode.blueharvest.R

@Suppress("DEPRECATION")
class SetUnit(private val context: Context) {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var unitSwitch: SwitchCompat

    init {
        initializeUnitSettings()
    }

    private fun initializeUnitSettings() {
        //Initialized the shared preferences and sets the XML components equal to the id in the XML file.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        // Find the SwitchCompat view from the layout inflated in the activity
        unitSwitch = SwitchCompat(context)
        unitSwitch.id = R.id.unitSwitch // You can set an ID programmatically if necessary

        // Set initial state of the switch
        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)
        unitSwitch.isChecked = toggleValue

        //Logic to change value of the unitToggleValue in the shared preferences when the unit toggle is switched.
        unitSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update SharedPreferences with the new unit preference
            sharedPreferences.edit().putBoolean("unitToggleValue", isChecked).apply()
        }
    }
}
