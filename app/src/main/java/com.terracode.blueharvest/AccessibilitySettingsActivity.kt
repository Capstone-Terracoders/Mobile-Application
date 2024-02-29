package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import java.util.Locale

//Activity class for the accessibility setting page.
class AccessibilitySettingsActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var unitSwitch: SwitchCompat
    private lateinit var languageSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessibility_settings)

        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)

        //Initialized the shared preferences and sets the XML components equal to the id in the XML file.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //-----Logic for Language Spinner-----//
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)

        val languages = resources.getStringArray(R.array.languageArray)
        languageSpinner = findViewById(R.id.languageSpinner)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, languages
        )
        languageSpinner.adapter = adapter

        languageSpinner.setSelection(currentLanguagePosition)

        languageSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val selectedLanguageCode = when (position) {
                    0 -> "en" // English
                    1 -> "fr" // French
                    2 -> "es" // Spanish
                    else -> "en" // Default to English if position is out of range
                }
                if (currentLanguagePosition != position) {
                    setLocale(selectedLanguageCode)
                    sharedPreferences.edit().putInt("selectedLanguagePosition", position).apply()
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        //-----Logic for Unit Switch-----//
        unitSwitch = findViewById(R.id.unitSwitch)

        // Set initial state of the switch
        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)
        unitSwitch.isChecked = toggleValue

        //Logic to change value of the unitToggleValue in the shared preferences when the unit toggle is switched.
        unitSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update SharedPreferences with the new unit preference
            sharedPreferences.edit().putBoolean("unitToggleValue", isChecked).apply()
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

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = Configuration(resources.configuration)

        // Set the new locale configuration
        configuration.setLocale(locale)

        // Create a new Context with the updated configuration
        val context = baseContext.createConfigurationContext(configuration)

        // Update the base context of the application
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)

        // Restart the activity to apply the changes
    }
}
