package com.terracode.blueharvest.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.utils.constants.HomeKeys
import com.terracode.blueharvest.utils.constants.Notification
import com.terracode.blueharvest.utils.constants.NotificationTypes
import com.terracode.blueharvest.utils.constants.PreferenceKeys
import com.terracode.blueharvest.utils.constants.TextConstants

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
            100)
    }

    fun getMaxHeightDisplayedInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.MAX_HEIGHT_DISPLAYED_INPUT.toString(),
            50)
    }

    fun getOptimalRPMRangeInput(): Float {
        return sharedPreferences.getFloat(
            PreferenceKeys.OPTIMAL_RPM_RANGE_INPUT.toString(),
            0f)
    }

    fun getOptimalHeightRangeInput(): Float {
        return sharedPreferences.getFloat(
            PreferenceKeys.OPTIMAL_HEIGHT_RANGE_INPUT.toString(),
            0f)
    }

    fun getMaxRakeRPMInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.MAX_RAKE_RPM.toString(),
            40)
    }

    fun getMinRakeHeightInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.MIN_RAKE_HEIGHT.toString(),
            0)
    }

    fun getRPMCoefficientInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.RPM_COEFFICIENT.toString(),
            0)
    }

    fun getHeightCoefficientInput(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.HEIGHT_COEFFICIENT.toString(),
            0)
    }

    fun getRecordButtonStatus(): Boolean {
        return sharedPreferences.getBoolean(
            HomeKeys.RECORD_BUTTON.toString(),
            false)
    }

    fun getNotifications(): List<Notification> {
        val notificationsSet = sharedPreferences.getStringSet(HomeKeys.NOTIFICATION.toString(), null)
        return notificationsSet?.mapNotNull { notificationString ->
            val parts = notificationString.split("|")
            if (parts.size == 3) {
                Notification(toNotification(parts[0]), parts[1], parts[2])
            } else {
                null
            }
        } ?: emptyList()
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

    fun setOptimalRPMRangeInput(input: Float) {
        sharedPreferences.edit().putFloat(
            PreferenceKeys.OPTIMAL_RPM_RANGE_INPUT.toString(),
            input).apply()
    }

    fun setOptimalHeightRangeInput(input: Float) {
        sharedPreferences.edit().putFloat(
            PreferenceKeys.OPTIMAL_HEIGHT_RANGE_INPUT.toString(),
            input).apply()
    }

    fun setMaxRakeRPMInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.MAX_RAKE_RPM.toString(),
            input).apply()
    }

    fun setMinRakeHeightInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.MIN_RAKE_HEIGHT.toString(),
            input).apply()
    }

    fun setRPMCoefficientInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.RPM_COEFFICIENT.toString(),
            input).apply()
    }

    fun setHeightCoefficientInput(input: Int) {
        sharedPreferences.edit().putInt(
            PreferenceKeys.HEIGHT_COEFFICIENT.toString(),
            input).apply()
    }

    fun setRecordButtonStatus(input: Boolean) {
        sharedPreferences.edit().putBoolean(
            HomeKeys.RECORD_BUTTON.toString(),
            input).apply()
    }

    fun setNotification(notification: Notification) {
        val notifications = getNotifications().toMutableList()
        notifications.add(notification)
        val notificationsSet =
            notifications.map { "${it.type}|${it.message}|${it.timestamp}" }.toSet()
        sharedPreferences.edit().putStringSet(HomeKeys.NOTIFICATION.toString(), notificationsSet)
            .apply()
    }

    /**
     * Clears all notifications from SharedPreferences.
     */
    fun clearNotifications() {
        sharedPreferences.edit().remove(HomeKeys.NOTIFICATION.toString()).apply()
    }

    //Enum to Int
    private inline fun <reified T : Enum<T>> T.toFloat(): Float {
        return this.ordinal.toFloat()
    }

    private fun toNotification(type: String): NotificationTypes {
        return when (type) {
            "NOTIFICATION" -> {
                NotificationTypes.NOTIFICATION
            }

            "WARNING" -> {
                NotificationTypes.WARNING
            }

            "ERROR" -> {
                NotificationTypes.ERROR
            }

            else -> {
                Log.d("PreferenceManager", "String type does not match type: NotificationType")
                throw IllegalArgumentException("Invalid type: $type")
            }
        }
    }
}
