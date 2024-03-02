package com.terracode.blueharvest.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

/**
 * Class to retrieve data values from a JSON object either from mock values or from Bluetooth.
 * The class provides methods to retrieve specific data values such as RPM, rake height, bush height, and linear speed.
 */
class ReadJSONObject(private val jsonObject: JSONObject) {

    /**
     * Retrieves the RPM (Revolutions Per Minute) data value from the JSON object.
     *
     * @return The RPM data value as a Double.
     */
    fun getRPM(): Double? {
        return jsonObject.getDouble("RPM")
    }

    /**
     * Retrieves the rake height data value from the JSON object.
     *
     * @return The rake height data value as a Double.
     */
    fun getRakeHeight(): Double? {
        return jsonObject.getDouble("rakeHeight")
    }

    /**
     * Retrieves the bush height data value from the JSON object.
     *
     * @return The bush height data value as a Double.
     */
    fun getBushHeight(): Double? {
        return jsonObject.getDouble("bushHeight")
    }

    /**
     * Retrieves the linear speed data value from the JSON object.
     *
     * @return The linear speed data value as a Double.
     */
    fun getSpeed(): Double? {
        return jsonObject.getDouble("speed")
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
    }
}
