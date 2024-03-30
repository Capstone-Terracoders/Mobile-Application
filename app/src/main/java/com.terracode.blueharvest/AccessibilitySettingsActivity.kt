package com.terracode.blueharvest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.terracode.blueharvest.services.accessibilityValueServices.ColorSchemeService
import com.terracode.blueharvest.services.accessibilityValueServices.LanguageService
import com.terracode.blueharvest.services.accessibilityValueServices.TextSizeService
import com.terracode.blueharvest.services.accessibilityValueServices.UnitService
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.LocaleManager
import com.terracode.blueharvest.utils.viewManagers.NotificationManager
import com.terracode.blueharvest.utils.viewManagers.TextSizeManager
import com.terracode.blueharvest.utils.viewManagers.ThemeManager

/**
 * Activity class for the Accessibility Settings Page
 *
 * @authors MacKenzie Young 3/2/2024
 *
 */
class AccessibilitySettingsActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var colorSpinner: Spinner
    private lateinit var languageSpinner: Spinner
    private lateinit var textSizeSeekBar: SeekBar
    private lateinit var unitSwitch: SwitchCompat

    private lateinit var notificationBellIcon: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences for this activity
        PreferenceManager.init(this)

        //Set setting values before setting the content view
        val currentTheme = ThemeManager.getCurrentTheme(this)
        ThemeManager.setColorOverlayTheme(this, currentTheme)

        val currentLanguagePosition = PreferenceManager.getSelectedLanguagePosition()
        val languagePosition = LocaleManager.getLanguageCode(currentLanguagePosition)
        LocaleManager.setLocale(this, languagePosition)

        //Set the view
        setContentView(R.layout.activity_accessibility_settings)

        //Set the text size for the view onCreate
        val rootView = findViewById<View>(android.R.id.content).rootView
        TextSizeManager.setTextSizeView(this, rootView)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Initialized variables:
        colorSpinner = findViewById(R.id.colorSchemeSpinner)
        languageSpinner = findViewById(R.id.languageSpinner)
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar)
        unitSwitch = findViewById(R.id.unitSwitch)

        //Initialize Services
        ColorSchemeService.setup(colorSpinner, this)
        LanguageService.setup(languageSpinner, this)
        TextSizeService.setup(textSizeSeekBar, this)
        UnitService.setup(unitSwitch, this)
    }

    //Inflates the menu in the toolbar.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
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

            R.id.notifications -> {
                // Sample notifications (replace with your actual notifications)
                val notifications = PreferenceManager.getNotifications()
                NotificationManager.showNotificationList(this, notificationBellIcon, notifications)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}