package com.terracode.blueharvest.utils

import com.terracode.blueharvest.utils.constants.UnitConstants


/**
 * Class to convert units from metric to imperial.
 * If needed, imperial to metric conversion can also be done here.
 * Since the data is sent via metric units, there is no need to convert back to metric as of 2/28/2024.
 * These functions format the units to 2 decimal places.
 *
 * @author MacKenzie Young 3/2/2024
 *
 */
class UnitConverter {
    companion object {
        /**
         * Function to convert height data values from metric to imperial.
         * It multiplies the current value by the conversion factor for centimeters to inches.
         *
         * @param currentValue The current height value in metric units (centimeters).
         * @return The converted height value in imperial units (inches) formatted to two decimal places,
         * or null if the input value is null.
         */
        fun convertHeightToImperial(currentValue: Float?): Float? {
            val cmToInch = UnitConstants.CM_TO_INCH.value
            return if (currentValue != null) {
                // Replace commas with periods and limit to two decimal places
                val formattedValue = currentValue.times(cmToInch)
                String.format("%.2f", formattedValue).replace(",", ".").toFloat()
            } else {
                null
            }
        }

        fun convertHeightToMetric(currentValue: Float?): Float? {
            val inchToCm = UnitConstants.INCH_TO_CM.value
            return if (currentValue != null) {
                // Replace commas with periods and limit to two decimal places
                val formattedValue = currentValue.times(inchToCm)
                String.format("%.2f", formattedValue).replace(",", ".").toFloat()
            } else {
                null
            }
        }

        /**
         * Function to convert speed data values from metric to imperial.
         * It multiplies the current value by the conversion factor for miles per hour to kilometers per hour.
         *
         * @param currentValue The current speed value in metric units (kilometers per hour).
         * @return The converted speed value in imperial units (miles per hour) formatted to two decimal places,
         * or null if the input value is null.
         */
        fun convertSpeedToImperial(currentValue: Float?): Float? {
            val mphToKmh = UnitConstants.MPH_TO_KMH.value

            return if (currentValue != null) {
                val formattedValue = currentValue.times(mphToKmh)
                String.format("%.2f", formattedValue).replace(",", ".").toFloat()
            } else {
                null
            }
        }
    }
}
