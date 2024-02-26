package com.terracode.blueharvest.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

class ReadJSONObject (private val jsonObject: JSONObject){
    fun getRPM(): Double? {
        return jsonObject.getDouble("RPM")
    }

    fun getRakeHeight(): Double? {
        return jsonObject.getDouble("RakeHeight")
    }

    fun getBushHeight(): Double? {
        return jsonObject.getDouble("BushHeight")
    }

    fun getSpeed(): Double? {
        return jsonObject.getDouble("Speed")
    }

    companion object{
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