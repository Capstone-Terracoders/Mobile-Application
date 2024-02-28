package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager


class AccessibilitySettingsActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var unitSwitch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessibility_settings)

        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        unitSwitch = findViewById(R.id.unitSwitch)

        // Set initial state of the switch
        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)
        unitSwitch.isChecked = toggleValue

        unitSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update SharedPreferences with the new unit preference
            sharedPreferences.edit().putBoolean("unitToggleValue", isChecked).apply()
           // sharedPreferences.edit().putBoolean("onStart", true).apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.backButton -> {
                val home = Intent(this, HomeActivity::class.java)
                startActivity(home)
                true
            }
            R.id.configurationSettings -> {
                val operationSettings = Intent(this, ConfigurationSettingsActivity::class.java)
                startActivity(operationSettings)
                true
            }
            R.id.accessibilitySettings -> {
                true // Do nothing, already in accessibility settings
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
