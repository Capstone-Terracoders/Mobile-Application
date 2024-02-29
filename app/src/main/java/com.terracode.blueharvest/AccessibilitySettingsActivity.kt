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

//Activity class for the accessibility setting page.
class AccessibilitySettingsActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var unitSwitch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessibility_settings)

        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)

        //Initialized the shared preferences and sets the unit switch equal to the id in the XML file.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        unitSwitch = findViewById(R.id.unitSwitch)

        // Set initial state of the switch
        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)
        unitSwitch.isChecked = toggleValue

        //Logic to change value of the unitToggleValue in the shared preferences when the unit toggle is switched.
        unitSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update SharedPreferences with the new unit preference
            sharedPreferences.edit().putBoolean("unitToggleValue", isChecked).apply()
           // sharedPreferences.edit().putBoolean("onStart", true).apply()
        }
    }

    //Inflates the menu in the toolbar.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    //Logic for the different menu options (what activity to inflate).
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
