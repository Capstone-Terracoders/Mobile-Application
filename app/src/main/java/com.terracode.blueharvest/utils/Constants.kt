package com.terracode.blueharvest.utils

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
enum class UnitConstants(val value: Double) {
    CM_TO_INCH(0.3937),
    MPH_TO_KMH(0.621371),
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
    MIN_TEXT_SIZE(16f),
    MAX_TEXT_SIZE(50f),
    DEFAULT_TEXT_SIZE(16f)
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
    HEIGHT_COEFFICIENT
}
