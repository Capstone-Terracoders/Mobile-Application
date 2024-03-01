package com.terracode.blueharvest.accessibilitySettings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.LocaleList
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.terracode.blueharvest.R
import java.util.Locale

@Suppress("DEPRECATION")
class SetLanguage(private val context: Context) {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageSpinner: Spinner

    init {
        initializeLanguageSettings()
    }

    private fun initializeLanguageSettings() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        //-----Logic for Language Spinner-----//
        val currentLanguagePosition = sharedPreferences.getInt("selectedLanguagePosition", 0)
        setLocale(getLanguageCode(currentLanguagePosition))

        val languages = context.resources.getStringArray(R.array.languageArray)
        languageSpinner = Spinner(context)
        val languageAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, languages
        )
        languageSpinner.adapter = languageAdapter

        languageSpinner.setSelection(currentLanguagePosition)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguageCode = getLanguageCode(position)
                if (currentLanguagePosition != position) {
                    setLocale(selectedLanguageCode)
                    sharedPreferences.edit().putInt("selectedLanguagePosition", position)
                        .apply()
                    // You cannot call recreate() here, you need an Activity reference
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if nothing is selected - required for object
            }
        }
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

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)

        context.resources.updateConfiguration(
            configuration,
            context.resources.displayMetrics
        )
    }
}
