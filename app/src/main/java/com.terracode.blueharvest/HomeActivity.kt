package com.terracode.blueharvest

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.services.homeServices.RecordButtonService
import com.terracode.blueharvest.utils.Notification
import com.terracode.blueharvest.utils.NotificationTypes
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.ReadJSONObject
import com.terracode.blueharvest.utils.UnitConverter
import com.terracode.blueharvest.utils.viewManagers.LocaleManager
import com.terracode.blueharvest.utils.viewManagers.NotificationManager
import com.terracode.blueharvest.utils.viewManagers.TextSizeManager
import com.terracode.blueharvest.utils.viewManagers.ThemeManager

/**
 * Activity class for the Accessibility Settings Page
 *
 * @authors MacKenzie Young
 * Last Updated: 3/2/2024
 *
 */
class HomeActivity : AppCompatActivity() {

    //Declaring the TextViews for the data values as TextView type.
    private lateinit var optimalRakeHeightTextView: TextView
    private lateinit var optimalRakeRPMValueTextView: TextView
    private lateinit var currentBushHeightTextView: TextView
    private lateinit var currentSpeedTextView: TextView
    private lateinit var recordButton: Button
    private lateinit var notificationButton: View

    //Declaring the data values
    private var bushHeightData: Double? = null
    private var rakeHeightData: Double? = null
    private var rpmData: Double? = null
    private var speedData: Double? = null

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
        setContentView(R.layout.activity_home)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.homeToolbar)
        setSupportActionBar(toolbar)

        //Set the text size
        val rootView = findViewById<View>(android.R.id.content).rootView
        TextSizeManager.setTextSizeView(this, rootView)

        //Set the declared TextView values equal to the IDs in the HomeActivity XML file.
        optimalRakeHeightTextView = findViewById(R.id.optimalRakeHeightValue)
        optimalRakeRPMValueTextView = findViewById(R.id.optimalRakeRPMValue)
        currentBushHeightTextView = findViewById(R.id.currentBushHeightValue)
        currentSpeedTextView = findViewById(R.id.currentSpeedValue)
        recordButton = findViewById(R.id.recordButton)
        notificationButton = findViewById(R.id.notifications)

        // Calls Record Data Service
        RecordButtonService.setup(recordButton, this)

        //Get the value of the toggle from AccessibilitySettings XML file.
        val toggleValue = PreferenceManager.getSelectedUnit()

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

        optimalRakeRPMValueTextView.text = "$rpmData"

        currentBushHeightTextView.text = if (toggleValue) {
            "$bushHeightData cm"
        } else {
            "${UnitConverter.convertHeightToImperial(bushHeightData)} in"
        }

        currentSpeedTextView.text = if (toggleValue) {
            "$speedData km/h"
        } else {
            //Needs function
            "${UnitConverter.convertSpeedToImperial(speedData)} mph"
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
                // Sample notifications (replace with your actual notifications)
                val notifications = PreferenceManager.getNotifications()
                NotificationManager.showNotificationList(this,notificationButton, notifications)
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
