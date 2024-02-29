package com.terracode.blueharvest.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

//Class to get data values from the JSON object either from mock values or from bluetooth.
class ReadJSONObject (private val jsonObject: JSONObject){
   //Gets the RPM data value.
    fun getRPM(): Double? {
        return jsonObject.getDouble("RPM")
    }

    //Gets the rake height data value.
    fun getRakeHeight(): Double? {
        return jsonObject.getDouble("rakeHeight")
    }

    //Gets the bush height data value.
    fun getBushHeight(): Double? {
        return jsonObject.getDouble("bushHeight")
    }

    //Gets the linear speed data value.
    fun getSpeed(): Double? {
        return jsonObject.getDouble("speed")
    }

    companion object{
        //Function to read file from assets folder, open the file and read the JSON object
        //Returning the json object, or error if there is one.
        fun fromAsset(context: Context, fileName: String): ReadJSONObject? {
            return try {
                val jsonString = context.assets.open(fileName).bufferedReader().use {
                    it.readText()
                }
                val jsonObject = JSONObject(jsonString)
                ReadJSONObject (jsonObject)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                null
            }

        }
    }
}