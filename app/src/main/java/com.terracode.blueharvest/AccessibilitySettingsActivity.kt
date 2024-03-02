package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors.applyToActivityIfAvailable
import com.google.android.material.color.DynamicColorsOptions
import com.terracode.blueharvest.utils.TextConstants
import com.terracode.blueharvest.utils.ThemeHelper
import java.util.Locale
import com.terracode.blueharvest.utils.ThemeHelper.getThemeResource
import kotlin.math.min

//Activity class for the accessibility setting page.
@Suppress("DEPRECATION")
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

        val toolbar: Toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)
        //Initialized the shared preferences and sets the XML components equal to the id in the XML file.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //-----Logic for Text Size Seek Bar-----//
        // Initialize SeekBar and set its properties
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar)
        val maxTextSize = TextConstants.MAX_TEXT_SIZE.value.toInt()

        // Set the maximum value of the SeekBar to reflect the maximum text size
        textSizeSeekBar.max = (maxTextSize)

        // Set the initial progress based on stored preferences
        val initialTextSize = sharedPreferences.getFloat("selectedTextSize", 16f)
        textSizeSeekBar.progress = initialTextSize.toInt()

        textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Apply the selected text size globally
                applyTextSize(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })



        //-----Logic for Language Spinner-----//
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        setLocale(getLanguageCode(currentLanguagePosition))

        val languages = resources.getStringArray(R.array.languageArray)
        languageSpinner = findViewById(R.id.languageSpinner)
        val languageAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, languages
        )
        languageSpinner.adapter = languageAdapter

        languageSpinner.setSelection(currentLanguagePosition)

        languageSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val selectedLanguageCode = getLanguageCode(position)
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


    private fun getLanguageCode(position: Int): String {
        return when (position) {
            0 -> "en" // English
            1 -> "fr" // French
            2 -> "es" // Spanish
            else -> "en" // Default to English if position is out of range
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = Configuration(resources.configuration)
        // Set the new locale configuration
        configuration.setLocale(locale)

        // Update the base context of the application
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)
    }
    private fun applyTextSize(textSize: Float) {
        // Implement logic to apply text size changes globally
        // For example, you can iterate through your TextViews and set their text sizes accordingly
        val minTextSize = TextConstants.MIN_TEXT_SIZE.value
        val maxTextSize = TextConstants.MAX_TEXT_SIZE.value
        val finalTextSize = maxOf(minTextSize, min(maxTextSize, textSize))

        val rootView = window.decorView.rootView
        setViewTextSize(rootView, finalTextSize)

        // Save the selected text size to preferences
        sharedPreferences.edit().putFloat("selectedTextSize", finalTextSize).apply()
    }

    private fun setViewTextSize(view: View, textSize: Float) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setViewTextSize(child, textSize)
            }
        } else if (view is TextView) {
            view.textSize = textSize
        }
    }
}