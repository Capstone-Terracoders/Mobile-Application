package com.terracode.blueharvest.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Singleton object for managing SharedPreferences in the application.
 */
object PreferenceManager {

    // SharedPreferences instance to manage preferences
    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Initializes the PreferenceManager with the application context.
     *
     * @param context The application context.
     */
    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    // Getters ----------------------------------------------------------

    /**
     * Retrieves the selected color position from SharedPreferences.
     *
     * @return The selected color position.
     */
    fun getSelectedColorPosition(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.SELECTED_COLOR_POSITION.toString(),
            0)
    }

    /**
     * Retrieves the selected language position from SharedPreferences.
     *
     * @return The selected language position.
     */
    fun getSelectedLanguagePosition(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.SELECTED_LANGUAGE_POSITION.toString(),
            0)
    }

    /**
     * Retrieves the selected text size from SharedPreferences.
     *
     * @return The selected text size.
     */
    fun getSelectedTextSize(): Float {
        return sharedPreferences.getFloat(
            PreferenceKeys.SELECTED_TEXT_SIZE.toString(),
            TextConstants.DEFAULT_TEXT_SIZE.toFloat())
    }

    /**
     * Retrieves the selected unit from SharedPreferences.
     *
     * @return The selected unit.
     */
    fun getSelectedUnit(): Boolean {
        return sharedPreferences.getBoolean(
            PreferenceKeys.SELECTED_UNIT.toString(),
            true)
    }

    fun getMaxRPMDisplayedInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.MAX_RPM_DISPLAYED_INPUT.toString(),
            0)
    }

    fun getMaxHeightDisplayedInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.MAX_HEIGHT_DISPLAYED_INPUT.toString(),
            0)
    }

    fun getOptimalRPMRangeInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.OPTIMAL_RPM_RANGE_INPUT.toString(),
            0)
    }

    fun getOptimalHeightRangeInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.OPTIMAL_HEIGHT_RANGE_INPUT.toString(),
            0)
    }

    // Setters ----------------------------------------------------------

    /**
     * Sets the selected color position in SharedPreferences.
     *
     * @param position The position of the selected color.
     */
    fun setSelectedColorPosition(position: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.SELECTED_COLOR_POSITION.toString(),
            position).apply()
    }

    /**
     * Sets the selected language position in SharedPreferences.
     *
     * @param position The position of the selected language.
     */
    fun setSelectedLanguagePosition(position: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.SELECTED_LANGUAGE_POSITION.toString(),
            position).apply()
    }

    /**
     * Sets the selected text size in SharedPreferences.
     *
     * @param finalTextSize The value of the selected text size.
     */
    fun setSelectedTextSize(finalTextSize: Float) {
        sharedPreferences.edit().putFloat(
            PreferenceKeys.SELECTED_TEXT_SIZE.toString(),
            finalTextSize).apply()
    }

    /**
     * Sets the selected unit in SharedPreferences.
     *
     * @param isChecked The value of the unit switch.
     */
    fun setSelectedUnit(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean(
            PreferenceKeys.SELECTED_UNIT.toString(),
            isChecked).apply()
    }

    fun setMaxRPMDisplayedInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.MAX_RPM_DISPLAYED_INPUT.toString(),
            input).apply()
    }

    fun setMaxHeightDisplayedInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.MAX_HEIGHT_DISPLAYED_INPUT.toString(),
            input).apply()
    }

    fun setOptimalRPMRangeInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.OPTIMAL_RPM_RANGE_INPUT.toString(),
            input).apply()
    }

    fun setOptimalHeightRangeInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.OPTIMAL_HEIGHT_RANGE_INPUT.toString(),
            input).apply()
    }

    //Enum to Int
    private inline fun <reified T : Enum<T>> T.toFloat(): Float {
        return this.ordinal.toFloat()
    }
}
