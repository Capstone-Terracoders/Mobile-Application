package com.terracode.blueharvest.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.terracode.blueharvest.BluetoothBle.BleDevice

/**
 * Singleton object for managing SharedPreferences in the application.
 */
object PreferenceManager {

    // SharedPreferences instance to manage preferences
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    /**
     * Initializes the PreferenceManager with the application context.
     *
     * @param context The application context.
     */
    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    // Getters ----------------------------------------------------------

    fun getFoundDevices(): MutableList<BleDevice>? {
        val json = sharedPreferences.getString(PreferenceKeys.FOUND_DEVICES.toString(), null)
        return gson.fromJson(json, object : TypeToken<MutableList<BleDevice>>() {}.type)
    }

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
    /**
     * Retrieves if scan button is pressed from SharedPreferences.
     *
     * @return The selected unit.
     */
    fun getScanIsPressed() : Boolean{
        return sharedPreferences.getBoolean(
            PreferenceKeys.SELECTED_START_SCAN.toString(),
            false)

    }

    // Setters ----------------------------------------------------------
    fun setFoundDevices(devices: MutableList<BleDevice>?) {
        val json = gson.toJson(devices)
        sharedPreferences.edit().putString(PreferenceKeys.FOUND_DEVICES.toString(), json).apply()
    }

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
    /**
     * Sets the selected unit in SharedPreferences.
     *
     * @param isPressed The state of the scanbutton.
     */
    fun setScanIsPressed(isPressed: Boolean){
        sharedPreferences.edit().putBoolean(
            PreferenceKeys.SELECTED_UNIT.toString(),
            isPressed).apply()
    }

    //Enum to Int
    private inline fun <reified T : Enum<T>> T.toFloat(): Float {
        return this.ordinal.toFloat()
    }
}
