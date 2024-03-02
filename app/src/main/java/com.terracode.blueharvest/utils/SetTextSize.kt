package com.terracode.blueharvest.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceManager

object SetTextSize {

    private fun applyTextSize(context: Context, view: View, textSize: Float) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                applyTextSize(context, child, textSize)
            }
        } else if (view is TextView) {
            view.textSize = textSize
        }
    }

    fun applyTextSizeFromPreferences(context: Context, view: View) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val textSize = sharedPreferences.getFloat("selectedTextSize", 16f)
        applyTextSize(context, view, textSize)
    }
}
