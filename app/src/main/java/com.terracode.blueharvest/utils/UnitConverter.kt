package com.terracode.blueharvest.utils


/**
 * Class to convert units from metric to imperial.
 * If needed, imperial to metric conversion can also be done here.
 * Since the data is sent via metric units, there is no need to convert back to metric as of 2/28/2024.
 * These functions format the units to 2 decimal places.
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
        fun convertHeightToImperial(currentValue: Double?): Double? {
            val cmToInch = UnitConstants.CM_TO_INCH.value
            return if (currentValue != null) {
                "%.2f".format(currentValue * cmToInch).toDouble()
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
        fun convertSpeedToImperial(currentValue: Double?): Double? {
            val mphToKmh = UnitConstants.MPH_TO_KMH.value
            return if (currentValue != null) {
                "%.2f".format(currentValue * mphToKmh).toDouble()
            } else {
                null
            }
        }
    }
}
