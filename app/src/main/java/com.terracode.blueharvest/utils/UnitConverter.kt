package com.terracode.blueharvest.utils


//Class to convert units from metric to imperial.
//If needed, imperial to metric conversion can also be done here.
//Since we are sending the data via metric units, we do not have a need to convert back to metric as of 2/28/2024.
//These functions format the units to 2 decimal places.
class UnitConverter {
    companion object {
        //Function to convert height data values from metric to imperial.
        fun convertHeightToImperial(currentValue: Double?): Double? {
            val cmToInch  = Constants.CM_TO_INCH.value
            return if (currentValue != null) {
                "%.2f".format(currentValue * cmToInch ).toDouble()
            } else {
                null
            }
        }

        //Function to convert speed data values from metric to imperial/
        fun convertSpeedToImperial(currentValue: Double?): Double? {
            val mphToKmh  = Constants.MPH_TO_KMH.value
            return if (currentValue != null) {
                "%.2f".format(currentValue * mphToKmh ).toDouble()
            } else {
                null
            }
        }
    }
}