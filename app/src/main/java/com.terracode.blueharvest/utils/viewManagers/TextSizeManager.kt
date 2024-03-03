package com.terracode.blueharvest.utils.viewManagers

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.terracode.blueharvest.utils.PreferenceManager


/**
 * Utility object for managing text size in all the activities in the application.
 * This object provides functions to apply text size preferences to views.
 *
 * @author MacKenzie Young 3/2/2024
 *
 */
object TextSizeManager {
    /**
     * Applies the specified text size to the TextViews within the given view hierarchy.
     * If the view is a ViewGroup, recursively applies the text size to its children.
     * If the view is a TextView, sets its text size.
     *
     * @param context The context used to access resources and preferences.
     * @param view The root view of the view hierarchy to apply text size to.
     */
    fun setTextSizeView(context: Context, view: View) {
        // Get the preference manager value for the text size and apply the text size.
        // Set SharedPreferences for this activity
        PreferenceManager.init(context)
        val textSize = PreferenceManager.getSelectedTextSize()
        setTextSize(context, view, textSize)
    }

    /**
     * Applies the specified text size to the TextViews within the given view hierarchy.
     * If the view is a ViewGroup, recursively applies the text size to its children.
     * If the view is a TextView, sets its text size.
     *
     * @param context The context used to access resources and preferences.
     * @param view The root view of the view hierarchy to apply text size to.
     * @param textSize The text size to be applied.
     */
    fun setTextSize(context: Context, view: View, textSize: Float) {
        // If the view is a ViewGroup, recursively apply text size to its children
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setTextSize(context, child, textSize)
            }
        }
        // If the view is a TextView, set its text size
        else if (view is TextView) {
            view.textSize = textSize
        }
    }
}