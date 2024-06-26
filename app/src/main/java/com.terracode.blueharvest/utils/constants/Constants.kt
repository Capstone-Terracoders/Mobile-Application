package com.terracode.blueharvest.utils.constants

/**
 * Enum class to declare conversion constants for units.
 * Each constant represents a conversion factor from one unit to another.
 *
 * @author MacKenzie Young 3/2/2024
 *
 * @property value The value representing the conversion factor.
 *
 * @value CM_TO_INCH Conversion factor from centimeters to inches.
 * @value MPH_TO_KMH Conversion factor from miles per hour to kilometers per hour.
 */
enum class UnitConstants(val value: Float) {
    CM_TO_INCH(0.3937F),
    INCH_TO_CM(2.54F),
    MPH_TO_KMH(0.621371F),
}

/**
 * Enum class to declare text size constants.
 * Each constant represents a specific text size value.
 *
 * @property value The value representing the text size.
 *
 * @value MIN_TEXT_SIZE Minimum text size constant.
 * @value MAX_TEXT_SIZE Maximum text size constant.
 */
enum class TextConstants(val value: Float) {
    MIN_TEXT_SIZE(12f),
    MAX_TEXT_SIZE(32f),
    DEFAULT_TEXT_SIZE(20f)
}

enum class PreferenceKeys {
    //---Accessibility Settings---//
    SELECTED_COLOR_POSITION,
    SELECTED_LANGUAGE_POSITION,
    SELECTED_TEXT_SIZE,
    SELECTED_UNIT,

    //---Display Values---//
    MAX_RPM_DISPLAYED_INPUT,
    MAX_HEIGHT_DISPLAYED_INPUT,
    OPTIMAL_RPM_RANGE_INPUT,
    OPTIMAL_HEIGHT_RANGE_INPUT,

    //---Safety Parameters---//
    MAX_RAKE_RPM,
    MIN_RAKE_HEIGHT,

    //---Operation Parameters---//
    RPM_COEFFICIENT,
    HEIGHT_COEFFICIENT,

    //---Operation Parameters---//
    WHEEL_RADIUS,
    RAKE_RADIUS,

    //---Bluetooth---//
    MY_BLE_STARTED,

    //---Symposium Parameters---//
    CURRENT_HEIGHT,
    CURRENT_RPM

}

enum class HomeKeys {
    RECORD_BUTTON,
    NOTIFICATION
}

enum class NotificationTypes {
    NOTIFICATION,
    WARNING,
    ERROR
}

enum class MaxUserInputInt(val value: Int) {
    MAX_DEFAULT_INPUT(100000),
    MAX_HEIGHT_INPUT(300),
    //For these, convert 10 meters to metric (cm) or imperial (in)
    MAX_WHEEL_INPUT_METRIC(1000),
    MAX_WHEEL_INPUT_IMPERIAL(400)

}

enum class MaxUserInputString(val value: String) {
    MAX_DEFAULT_INPUT("100,000"),
    MAX_HEIGHT_INPUT("300"),
    MAX_WHEEL_INPUT_METRIC("1000"),
    MAX_WHEEL_INPUT_IMPERIAL("400")
}
