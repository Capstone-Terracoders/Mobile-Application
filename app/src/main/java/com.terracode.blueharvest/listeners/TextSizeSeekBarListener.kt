package com.terracode.blueharvest.listeners


import android.content.Context
import android.widget.SeekBar
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.managers.TextSizeManager
import com.terracode.blueharvest.utils.TextConstants
import kotlin.math.min


class TextSizeChangeListener(private val activity: AccessibilitySettingsActivity) :
    SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            val textSize = progress.toFloat()
            applyText(activity, textSize)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun applyText(context: Context, textSize: Float) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        // Implement logic to apply text size changes globally
        val minTextSize = TextConstants.MIN_TEXT_SIZE.value
        val maxTextSize = TextConstants.MAX_TEXT_SIZE.value
        val finalTextSize = maxOf(minTextSize, min(maxTextSize, textSize))

        // Get the root view using context to avoid potential errors
        val rootView = activity.window.decorView.rootView

        TextSizeManager.setTextSize(context, rootView, finalTextSize)

        // Save the selected text size to preferences
        sharedPreferences.edit().putFloat("selectedTextSize", finalTextSize).apply()
    }


}

