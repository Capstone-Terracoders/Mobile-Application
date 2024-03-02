package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors.applyToActivityIfAvailable
import com.google.android.material.color.DynamicColorsOptions
import com.terracode.blueharvest.listeners.LanguageSelectionListener
import com.terracode.blueharvest.listeners.TextSizeChangeListener
import com.terracode.blueharvest.utils.SetTextSize
import com.terracode.blueharvest.utils.TextConstants
import com.terracode.blueharvest.utils.ThemeHelper
import com.terracode.blueharvest.utils.ThemeHelper.getThemeResource

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
        setColorOverlayTheme(ThemeHelper.getCurrentTheme(this))
        setContentView(R.layout.activity_accessibility_settings)

        //Set the text size for the view onCreate
        val rootView = findViewById<View>(android.R.id.content).rootView
        SetTextSize.setTextSizeView(this, rootView)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)

        //Initialized variables:
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar)
        languageSpinner = findViewById(R.id.languageSpinner)

        //Initialize Listeners
        val languageListener = LanguageSelectionListener(this)
        val textSizeChangeListener = TextSizeChangeListener(this)

        //-----Logic for Text Size Seek Bar-----//
        // Initialize SeekBar properties
        val initialTextSize = sharedPreferences.getFloat("selectedTextSize", 16f)
        val maxTextSize = TextConstants.MAX_TEXT_SIZE.value.toInt()
        textSizeSeekBar.max = (maxTextSize)
        textSizeSeekBar.progress = initialTextSize.toInt()
        textSizeSeekBar.setOnSeekBarChangeListener(textSizeChangeListener)


        //-----Logic for Language Spinner in Activity-----//
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        val languagePosition = languageListener.getLanguageCode(currentLanguagePosition)
        languageListener.setLocale(languagePosition)

        val languages = resources.getStringArray(R.array.languageArray)
        val languageAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, languages
        )
        languageSpinner.adapter = languageAdapter

        languageSpinner.setSelection(currentLanguagePosition)

        languageSpinner.onItemSelectedListener = languageListener



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

        //-----Logic for Color Scheme-----//
        val currentColorPosition = sharedPreferences.getInt("selectedColorPosition", 0)

        val colors = resources.getStringArray(R.array.colorSchemeArray)
        colorSpinner = findViewById(R.id.colorSchemeSpinner)
        val colorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, colors
        )
        colorSpinner.adapter = colorAdapter

        colorSpinner.setSelection(currentColorPosition)

        colorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val selectedColorTheme = getThemeResource(position)
                if (currentColorPosition != position) {
                    setColorOverlayTheme(selectedColorTheme)
                    sharedPreferences.edit().putInt("selectedColorPosition", position).apply()
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
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

    private fun setColorOverlayTheme(colorOverlay: Int) {
        val options = DynamicColorsOptions.Builder().setThemeOverlay(colorOverlay).build()
        applyToActivityIfAvailable(this, options)
    }
}