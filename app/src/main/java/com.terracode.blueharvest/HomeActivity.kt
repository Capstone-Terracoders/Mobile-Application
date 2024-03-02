package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.utils.LocaleHelper
import com.terracode.blueharvest.utils.ReadJSONObject
import com.terracode.blueharvest.utils.UnitConverter
import com.terracode.blueharvest.utils.SetTextSize
import com.terracode.blueharvest.utils.ThemeHelper

class HomeActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var sharedPreferences: SharedPreferences
    //Declaring the TextViews for the data values as TextView type.
    private lateinit var optimalRakeHeightTextView: TextView
    private lateinit var optimalRakeRPMValueTextView: TextView
    private lateinit var currentBushHeightTextView: TextView
    private lateinit var currentSpeedTextView: TextView

    //Declaring the data values
    private var bushHeightData: Double? = null
    private var rakeHeightData: Double? = null
    private var rpmData: Double? = null
    private var speedData: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //Set setting values before setting the content view
        val currentTheme = ThemeHelper.getCurrentTheme(this)
        ThemeHelper.setColorOverlayTheme(this, currentTheme)

        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        val languagePosition = LocaleHelper.getLanguageCode(currentLanguagePosition)
        LocaleHelper.setLocale(this, languagePosition)

        //Set the view
        setContentView(R.layout.activity_home)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.homeToolbar)
        setSupportActionBar(toolbar)

        //Set the text size
        val rootView = findViewById<View>(android.R.id.content).rootView
        SetTextSize.setTextSizeView(this, rootView)

        //Set the declared TextView values equal to the IDs in the HomeActivity XML file.
        optimalRakeHeightTextView = findViewById(R.id.optimalRakeHeightValue)
        optimalRakeRPMValueTextView = findViewById(R.id.optimalRakeRPMValue)
        currentBushHeightTextView = findViewById(R.id.currentBushHeightValue)
        currentSpeedTextView = findViewById(R.id.currentSpeedValue)

        //Get the value of the toggle from AccessibilitySettings XML file.
        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)

        //Read data from mock values/bluetooth, and set the data values equal to the declared variables from above.
        val sensorData = ReadJSONObject.fromAsset(this, "SensorDataExample.json")
        sensorData?.apply {
            rpmData = getRPM()
            rakeHeightData = getRakeHeight()
            bushHeightData = getBushHeight()
            speedData = getSpeed()
        }

        //Set the value of the text on the XML file equal to the data values depending on if the toggle is switched.
        optimalRakeHeightTextView.text = if (toggleValue) {
            "$rakeHeightData cm"
        } else {
            "${UnitConverter.convertHeightToImperial(rakeHeightData)} in"
        }

        optimalRakeRPMValueTextView.text = if (toggleValue) {
            "$rpmData mph"
        } else {
            //Needs function
            "${UnitConverter.convertSpeedToImperial(rpmData)} kmh"
        }

        currentBushHeightTextView.text = if (toggleValue) {
            "$bushHeightData cm"
        } else {
            "${UnitConverter.convertHeightToImperial(bushHeightData)} in"
        }

        currentSpeedTextView.text = if (toggleValue) {
            "$speedData mph"
        } else {
            //Needs function
            "${UnitConverter.convertSpeedToImperial(speedData)} kmh"
        }
    }

    //Inflates the menu in the toolbar.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    //Logic for the different menu options (what activity to inflate).
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications
            R.id.notifications -> {
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
