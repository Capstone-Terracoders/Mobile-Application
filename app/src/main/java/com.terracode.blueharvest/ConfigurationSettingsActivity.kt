package com.terracode.blueharvest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.terracode.blueharvest.listeners.configurationListeners.MaxHeightDisplayListener
import com.terracode.blueharvest.listeners.configurationListeners.MaxRPMDisplayListener
import com.terracode.blueharvest.listeners.configurationListeners.OptimalHeightRangeListener
import com.terracode.blueharvest.listeners.configurationListeners.OptimalRPMRangeListener
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.LocaleManager
import com.terracode.blueharvest.utils.viewManagers.TextSizeManager
import com.terracode.blueharvest.utils.viewManagers.ThemeManager

/**
 * Activity class for the Configuration Settings Page
 *
 * @authors MacKenzie Young
 * Last Updated: 3/9/2024
 *
 */
class ConfigurationSettingsActivity : AppCompatActivity() {

    private lateinit var maxRPMDisplayedInput: EditText
    private lateinit var maxHeightDisplayedInput: EditText
    private lateinit var optimalRPMRangeInput: EditText
    private lateinit var optimalHeightRangeInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the sharedPreferences
        PreferenceManager.init(this)

        //Set setting values before setting the content view
        val currentTheme = ThemeManager.getCurrentTheme(this)
        ThemeManager.setColorOverlayTheme(this, currentTheme)

        val currentLanguagePosition = PreferenceManager.getSelectedLanguagePosition()
        val languagePosition = LocaleManager.getLanguageCode(currentLanguagePosition)
        LocaleManager.setLocale(this, languagePosition)

        //Set the view
        setContentView(R.layout.activity_configuration_settings)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)

        //Set the text size
        val rootView = findViewById<View>(android.R.id.content).rootView
        TextSizeManager.setTextSizeView(this, rootView)

        //Declaring View Components
        maxRPMDisplayedInput = findViewById(R.id.maxRPMDisplayedNumber)
        maxHeightDisplayedInput = findViewById(R.id.maxHeightDisplayedNumber)
        optimalRPMRangeInput = findViewById(R.id.optimalRPMRangeNumber)
        optimalHeightRangeInput = findViewById(R.id.optimalHeightRangeNumber)

        //Initialize Listeners
        val maxRPMDisplayedListener = MaxRPMDisplayListener(this)
        val maxHeightDisplayedListener = MaxHeightDisplayListener(this)
        val optimalRPMRangeListener = OptimalRPMRangeListener(this)
        val optimalHeightRangeListener = OptimalHeightRangeListener(this)

        //Logic for the maxRPMDisplayedInput
        // Get the value from PreferenceManager
        val maxRPMDisplayed: Int = PreferenceManager.getMaxRPMDisplayedInput()

        // Check if the value is not null before setting it to the EditText
        maxRPMDisplayed.let {
            // Convert the Int value to String since setText() expects a String
            maxRPMDisplayedInput.setText(it.toString())
        }

        maxRPMDisplayedInput.addTextChangedListener(maxRPMDisplayedListener)

        //Logic for the maxHeightDisplayedInput
        // Get the value from PreferenceManager
        val maxHeightDisplayed: Int = PreferenceManager.getMaxHeightDisplayedInput()

        // Check if the value is not null before setting it to the EditText
        maxHeightDisplayed.let {
            // Convert the Int value to String since setText() expects a String
            maxHeightDisplayedInput.setText(it.toString())
        }

        maxHeightDisplayedInput.addTextChangedListener(maxHeightDisplayedListener)

        //Logic for the optimalRPMRangeInput
        // Get the value from PreferenceManager
        val optimalRPMInput: Int = PreferenceManager.getOptimalRPMRangeInput()

        // Check if the value is not null before setting it to the EditText
        optimalRPMInput.let {
            // Convert the Int value to String since setText() expects a String
            optimalRPMRangeInput.setText(it.toString())
        }

        optimalRPMRangeInput.addTextChangedListener(optimalRPMRangeListener)

        //Logic for the optimalHeightRangeInput
        // Get the value from PreferenceManager
        val optimalHeightInput: Int = PreferenceManager.getOptimalHeightRangeInput()

        // Check if the value is not null before setting it to the EditText
        optimalHeightInput.let {
            // Convert the Int value to String since setText() expects a String
            optimalHeightRangeInput.setText(it.toString())
        }

        optimalHeightRangeInput.addTextChangedListener(optimalHeightRangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications
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
                val accessibilitySettings = Intent(this, AccessibilitySettingsActivity::class.java)
                startActivity(accessibilitySettings)
                true
            }

            else -> false
        }
    }
}
