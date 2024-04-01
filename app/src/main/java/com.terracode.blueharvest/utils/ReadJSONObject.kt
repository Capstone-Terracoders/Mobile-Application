package com.terracode.blueharvest.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

/**
 * Class to retrieve data values from a JSON object either from mock values or from Bluetooth.
 * The class provides methods to retrieve specific data values such as RPM, rake height, bush height, and linear speed.
 *
 * @author MacKenzie Young 3/2/2024
 *
 */
class ReadJSONObject(private val jsonObject: JSONObject) {

    /**
     * Retrieves the RPM (Revolutions Per Minute) data value from the JSON object.
     *
     * @return The RPM data value as a Double.
     */
    fun getRPM(): Double? {
        val rpmString = jsonObject.optString("RPM", "") // Retrieve RPM as a string
        return convertNumericValue(rpmString) // Convert string to Double
    }

    /**
     * Retrieves the rake height data value from the JSON object.
     *
     * @return The rake height data value as a Double.
     */
    fun getRakeHeight(): Double? {
        val rakeHeightString = jsonObject.optString("rakeHeight", "") // Retrieve rake height as a string
        return convertNumericValue(rakeHeightString) // Convert string to Double
    }

    /**
     * Retrieves the bush height data value from the JSON object.
     *
     * @return The bush height data value as a Double.
     */
    fun getBushHeight(): Double? {
        val bushHeightString = jsonObject.optString("bushHeight", "") // Retrieve bush height as a string
        return convertNumericValue(bushHeightString) // Convert string to Double
    }

    /**
     * Retrieves the linear speed data value from the JSON object.
     *
     * @return The linear speed data value as a Double.
     */
    fun getSpeed(): Double? {
        val speedString = jsonObject.optString("speed", "") // Retrieve speed as a string
        return convertNumericValue(speedString) // Convert string to Double
    }

    fun getOptimalRakeHeight(): Double? {
        val optimalRakeHeightString = jsonObject.optString("optimalRakeHeight", "") // Retrieve optimal rake height as a string
        return convertNumericValue(optimalRakeHeightString) // Convert string to Double
    }

    fun getOptimalRakeRPM(): Double? {
        val optimalRakeRpmString = jsonObject.optString("optimalRakeRPM", "") // Retrieve optimal rake height as a string
        return convertNumericValue(optimalRakeRpmString) // Convert string to Double
    }

    companion object {
        /**
         * Reads a file from the assets folder, opens the file, and reads the JSON object.
         * Returns the JSON object, or null if there is an error.
         *
         * @param context The context used to access the assets folder.
         * @param fileName The name of the JSON file to read.
         * @return A ReadJSONObject instance initialized with the JSON object read from the file, or null if there is an error.
         */
        fun fromAsset(context: Context, fileName: String): ReadJSONObject? {
            return try {
                val jsonString = context.assets.open(fileName).bufferedReader().use {
                    it.readText()
                }
                val jsonObject = JSONObject(jsonString)
                ReadJSONObject(jsonObject)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                null
            }
        }

        private fun convertNumericValue(value: String): Double? {
            val formattedValue = value.replace(",", ".") // Replace commas with periods
            return formattedValue.toDoubleOrNull() // Parse the string to a Double
        }
    }
}
