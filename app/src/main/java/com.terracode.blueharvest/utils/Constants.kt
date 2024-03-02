package com.terracode.blueharvest.utils

/**
 * Enum class to declare conversion constants for units.
 * Each constant represents a conversion factor from one unit to another.
 *
 * @property value The value representing the conversion factor.
 *
 * @value CM_TO_INCH Conversion factor from centimeters to inches.
 * @value MPH_TO_KMH Conversion factor from miles per hour to kilometers per hour.
 */
enum class UnitConstants(val value: Double) {
    CM_TO_INCH(0.3937),
    MPH_TO_KMH(1.609344),
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
    MAX_TEXT_SIZE(50f)
}
