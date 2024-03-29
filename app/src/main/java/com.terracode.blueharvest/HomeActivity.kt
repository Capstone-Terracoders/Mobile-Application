package com.terracode.blueharvest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.services.homeServices.RecordButtonService
import com.terracode.blueharvest.utils.PreferenceManager
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
    private lateinit var currentRPMTextView: TextView
    private lateinit var currentHeightTextView: TextView
    private lateinit var recordButton: Button
    private lateinit var notificationBellIcon: View

    //Declare value types
    private var cm = "cm"
    private var inch = "in"
    private var kmph = "km/h"
    private var mph = "mph"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the sharedPreferences
        PreferenceManager.init(this)

        //Set setting values before setting the content view
        val currentTheme = ThemeManager.getCurrentTheme(this)
        ThemeManager.setColorOverlayTheme(this, currentTheme)

        //Set language
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
        currentRPMTextView = findViewById(R.id.currentRpmValue)
        currentHeightTextView = findViewById(R.id.currentHeightValue)
        recordButton = findViewById(R.id.recordButton)
        notificationBellIcon = findViewById(R.id.notifications)

        // Calls Record Data Service
        RecordButtonService.setup(recordButton, this)

        //Get the value of the toggle from AccessibilitySettings XML file.
        val toggleValue = PreferenceManager.getSelectedUnit()

        //Read data from mock values/bluetooth, which we locate in the preference manager
        val rpmData = PreferenceManager.getRpm()
        val rakeHeightData = PreferenceManager.getRakeHeight()
        val bushHeightData = PreferenceManager.getBushHeight()
        val speedData = PreferenceManager.getSpeed()
        val optimalRakeHeight = PreferenceManager.getOptimalRakeHeight()
        val optimalRakeRpm = PreferenceManager.getOptimalRakeRpm()


        //CurrentValueTitles
        val maxHeightValue = PreferenceManager.getMaxHeightDisplayedInput()
        val minHeightSafetyValue = PreferenceManager.getMinRakeHeightInput()
        val maxRpmSafetyValue = PreferenceManager.getMaxRakeRPMInput()
        val optimalRpmRange = PreferenceManager.getOptimalRPMRangeInput()
        val optimalHeightRange = PreferenceManager.getOptimalHeightRangeInput()

        //Configure lower and upper ranges for optimal height and rpm
        val rpmUpperRange = optimalRakeRpm?.plus(optimalRpmRange)
        val rpmLowerRange = optimalRakeRpm?.minus(optimalRpmRange)

        val heightUpperRange = optimalRakeHeight?.plus(optimalHeightRange)
        val heightLowerRange = optimalRakeHeight?.minus(optimalHeightRange)

        //Setting Titles
        val currentHeightTitle = getString(R.string.currentHeightTitle)
        val currentRpmTitle = getString(R.string.currentRPMTitle)
        val currentRpmText = "$currentRpmTitle $rpmData"
        currentRPMTextView.text = currentRpmText

        //Settings colors
        val redColor = ContextCompat.getColor(this, R.color.red)
        val blackColor = ContextCompat.getColor(this, R.color.black)
        val darkGreenColor = ContextCompat.getColor(this, R.color.dark_green)

        //Current value text color logic - Height
        if (rakeHeightData != null) {
            if (rakeHeightData > maxHeightValue || rakeHeightData < minHeightSafetyValue) {
                currentHeightTextView.setTextColor(redColor)
            } else if (rakeHeightData > heightLowerRange!! && rakeHeightData < heightUpperRange!!) {
                currentHeightTextView.setTextColor(darkGreenColor)
            } else {
                currentHeightTextView.setTextColor(blackColor)
            }
        }

        //Current value text color logic - RPM
        if (rpmData!! > maxRpmSafetyValue || rpmData < 0) {
            currentRPMTextView.setTextColor(redColor)
        } else if (rpmData > rpmLowerRange!! && rpmData < rpmUpperRange!!) {
            currentRPMTextView.setTextColor(darkGreenColor)
        } else {
            currentRPMTextView.setTextColor(blackColor)
        }


        //Set the value of the text on the XML file equal to the data values depending on if the toggle is switched.
        if (toggleValue) {
            val optimalRakeHeightText = "$optimalRakeHeight $cm"
            optimalRakeHeightTextView.text = optimalRakeHeightText
            val optimalRakeRpmText = "$optimalRakeRpm $cm"
            optimalRakeRPMValueTextView.text = optimalRakeRpmText
            val currentBushHeightText = "$bushHeightData $cm"
            currentBushHeightTextView.text = currentBushHeightText
            val currentSpeedText = "$speedData $kmph"
            currentSpeedTextView.text = currentSpeedText
            val currentHeightText = "$currentHeightTitle $rakeHeightData $cm"
            currentHeightTextView.text = currentHeightText

        } else {
            val optimalRakeHeightText = "$optimalRakeHeight $inch"
            optimalRakeHeightTextView.text = optimalRakeHeightText
            val optimalRakeRpmText = "$optimalRakeRpm $inch"
            optimalRakeRPMValueTextView.text = optimalRakeRpmText
            val currentBushHeightText = "$bushHeightData $inch"
            currentBushHeightTextView.text = currentBushHeightText
            val currentSpeedText = "$speedData $mph"
            currentSpeedTextView.text = currentSpeedText
            val currentHeightText = "$currentHeightTitle $rakeHeightData $inch"
            currentHeightTextView.text = currentHeightText

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
                NotificationManager.showNotificationList(this, notificationBellIcon, notifications)
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
