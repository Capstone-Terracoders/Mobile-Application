package com.terracode.blueharvest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.terracode.blueharvest.listeners.MaxRPMDisplayListener
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
//        val maxHeightDisplayedListener = MaxHeightDisplayListener(this)
//        val optimalRPMRangeListener = OptimalRPMRangeListener(this)
//        val optimalHeightRangeListener = OptimalHeightRangeListener(this)

        //Logic for the maxRPMDisplayedInput
        maxRPMDisplayedInput.addTextChangedListener(maxRPMDisplayedListener)

        //Logic for the maxHeightDisplayedInput

        //Logic for the optimalRPMRangeInput

        //Logic for the optimalHeightRangeInput
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
