package com.terracode.blueharvest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.terracode.blueharvest.BluetoothBle.BluetoothBLEActivity
import com.terracode.blueharvest.services.displayValueServices.MaxHeightDisplayedService
import com.terracode.blueharvest.services.displayValueServices.MaxRPMDisplayedService
import com.terracode.blueharvest.services.optimalRangeValueServices.OptimalHeightRangeService
import com.terracode.blueharvest.services.optimalRangeValueServices.OptimalRPMRangeService
import com.terracode.blueharvest.services.operationValueServices.HeightCoefficientService
import com.terracode.blueharvest.services.operationValueServices.RPMCoefficientService
import com.terracode.blueharvest.services.safetyValueServices.MaxRakeRPMService
import com.terracode.blueharvest.services.safetyValueServices.MinRakeHeightService
import com.terracode.blueharvest.services.toolbarServices.BackButtonService
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.LocaleManager
import com.terracode.blueharvest.utils.viewManagers.NotificationManager
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

    private lateinit var maxRakeRPMInput: EditText
    private lateinit var minRakeHeightInput: EditText

    private lateinit var rpmCoefficientInput: EditText
    private lateinit var heightCoefficientInput: EditText

    private lateinit var notificationBellIcon: View
    private lateinit var backButton: Button


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
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Set the text size
        val rootView = findViewById<View>(android.R.id.content).rootView
        TextSizeManager.setTextSizeView(this, rootView)

        //Declaring Display Value View Components
        maxRPMDisplayedInput = findViewById(R.id.maxRPMDisplayedNumber)
        maxHeightDisplayedInput = findViewById(R.id.maxHeightDisplayedNumber)
        optimalRPMRangeInput = findViewById(R.id.optimalRPMRangeNumber)
        optimalHeightRangeInput = findViewById(R.id.optimalHeightRangeNumber)

        //Declaring Safety Param View Components
        maxRakeRPMInput = findViewById(R.id.maxRakeRPMNumber)
        minRakeHeightInput = findViewById(R.id.minHeightNumber)

        //Declaring Operation Param View Components
        rpmCoefficientInput = findViewById(R.id.coefficientRPMNumber)
        heightCoefficientInput = findViewById(R.id.coefficientHeightNumber)

        //Toolbar items
        backButton = findViewById(R.id.backButton)
        notificationBellIcon = findViewById(R.id.notifications)


        //Initialize Display Value Services
        MaxRPMDisplayedService.setup(maxRPMDisplayedInput, this)
        MaxHeightDisplayedService.setup(maxHeightDisplayedInput, this)
        OptimalRPMRangeService.setup(optimalRPMRangeInput, this)
        OptimalHeightRangeService.setup(optimalHeightRangeInput, this)

        //Initialize Safety Param Services
        MaxRakeRPMService.setup(maxRakeRPMInput, this)
        MinRakeHeightService.setup(minRakeHeightInput, this)

        //Initialize Operation Param Services
        RPMCoefficientService.setup(rpmCoefficientInput, this)
        HeightCoefficientService.setup(heightCoefficientInput, this)

        //Back Button Service
        BackButtonService.setup(backButton, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications

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

            R.id.notifications -> {
                // Sample notifications (replace with your actual notifications)
                val notifications = PreferenceManager.getNotifications()
                NotificationManager.showNotificationList(this, notificationBellIcon, notifications)
                true
            }

            R.id.bluetoothBLE -> {
                val bluetoothBLEActivity = Intent(this, BluetoothBLEActivity::class.java)
                startActivity(bluetoothBLEActivity)
                true
            }

            else -> false
        }
    }
}
