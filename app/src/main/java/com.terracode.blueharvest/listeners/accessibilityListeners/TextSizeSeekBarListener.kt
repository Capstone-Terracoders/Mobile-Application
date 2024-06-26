package com.terracode.blueharvest.listeners.accessibilityListeners

import android.content.Context
import android.widget.SeekBar
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.TextSizeManager

/**
 * Listener for handling changes in text size in the accessibility settings screen.
 * This listener is triggered when the user adjusts the text size using a SeekBar.
 *
 * @author MacKenzie Young 3/2/2024
 *
 * @param activity The parent AccessibilitySettingsActivity instance.
 */
class TextSizeChangeListener(private val activity: AccessibilitySettingsActivity) :
    SeekBar.OnSeekBarChangeListener {

    /**
     * Called when the user slides the SeekBar, resulting in a change in progress.
     *
     * @param seekBar The SeekBar whose progress has changed.
     * @param progress The current progress level.
     * @param fromUser True if the progress change was initiated by the user, false otherwise.
     */
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            val textSize = progress.toFloat()
            applyText(activity, textSize)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // No action needed when the user starts tracking touch on the SeekBar
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // No action needed when the user stops tracking touch on the SeekBar
    }

    /**
     * Apply the selected text size to the application.
     *
     * @param context The Context of the application.
     * @param textSize The size of the text to be applied.
     */
    private fun applyText(context: Context, textSize: Float) {
        // Set SharedPreferences for this activity
        PreferenceManager.init(activity)

        // Get the root view using the activity's window decor view
        val rootView = activity.window.decorView.rootView

        // Set the text size across the application
        TextSizeManager.setTextSize(context, rootView, textSize)

        // Save the selected text size to preferences
        PreferenceManager.setSelectedTextSize(textSize)
    }
}

