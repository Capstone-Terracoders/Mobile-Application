package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.listeners.ColorSchemeListener
import com.terracode.blueharvest.listeners.LanguageSelectionListener
import com.terracode.blueharvest.listeners.TextSizeChangeListener
import com.terracode.blueharvest.listeners.UnitToggleListener
import com.terracode.blueharvest.managers.LocaleManager
import com.terracode.blueharvest.managers.TextSizeManager
import com.terracode.blueharvest.utils.TextConstants
import com.terracode.blueharvest.managers.ThemeManager

//Activity class for the accessibility setting page.
class AccessibilitySettingsActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var unitSwitch: SwitchCompat
    private lateinit var languageSpinner: Spinner
    private lateinit var colorSpinner: Spinner
    private lateinit var textSizeSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //Set setting values before setting the content view
        val currentTheme = ThemeManager.getCurrentTheme(this)
        ThemeManager.setColorOverlayTheme(this, currentTheme)

        //Set the view
        setContentView(R.layout.activity_accessibility_settings)

        //Set the text size for the view onCreate
        val rootView = findViewById<View>(android.R.id.content).rootView
        TextSizeManager.setTextSizeView(this, rootView)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)

        //Initialized variables:
        colorSpinner = findViewById(R.id.colorSchemeSpinner)
        languageSpinner = findViewById(R.id.languageSpinner)
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar)
        unitSwitch = findViewById(R.id.unitSwitch)

        //Initialize Listeners
        val colorSchemeListener = ColorSchemeListener(this)
        val languageListener = LanguageSelectionListener(this)
        val textSizeChangeListener = TextSizeChangeListener(this)
        val unitToggleListener = UnitToggleListener(this)


        //-----Logic for Color Scheme-----//
        val currentColorPosition = sharedPreferences.getInt("selectedColorPosition", 0)

        val colors = resources.getStringArray(R.array.colorSchemeArray)
        val colorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, colors
        )
        colorSpinner.adapter = colorAdapter

        colorSpinner.setSelection(currentColorPosition)

        colorSpinner.onItemSelectedListener = colorSchemeListener


        //-----Logic for Language Spinner in Activity-----//
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        val languagePosition = LocaleManager.getLanguageCode(currentLanguagePosition)
        LocaleManager.setLocale(this, languagePosition)

        val languages = resources.getStringArray(R.array.languageArray)
        val languageAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, languages
        )
        languageSpinner.adapter = languageAdapter

        languageSpinner.setSelection(currentLanguagePosition)

        languageSpinner.onItemSelectedListener = languageListener


        //-----Logic for Text Size Seek Bar-----//
        // Initialize SeekBar properties
        val initialTextSize = sharedPreferences.getFloat("selectedTextSize", 16f)
        val maxTextSize = TextConstants.MAX_TEXT_SIZE.value.toInt()
        textSizeSeekBar.max = (maxTextSize)
        textSizeSeekBar.progress = initialTextSize.toInt()
        textSizeSeekBar.setOnSeekBarChangeListener(textSizeChangeListener)


        //-----Logic for Unit Switch-----//
        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)
        unitSwitch.isChecked = toggleValue

        //Logic to change value of the unitToggleValue in the shared preferences when the unit toggle is switched.
        unitSwitch.setOnCheckedChangeListener(unitToggleListener)
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