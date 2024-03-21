package com.terracode.blueharvest.services.accessibilityValueServices

import android.widget.SeekBar
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.listeners.accessibilityListeners.TextSizeChangeListener
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.TextConstants

object  TextSizeService {
    fun setup(textSizeSeekBar: SeekBar, activity: AccessibilitySettingsActivity) {
        PreferenceManager.init(activity)

        val textSizeChangeListener = TextSizeChangeListener(activity)
        val initialTextSize = PreferenceManager.getSelectedTextSize()

        val maxTextSize = TextConstants.MAX_TEXT_SIZE.value.toInt()
        val minTextSize = TextConstants.MIN_TEXT_SIZE.value.toInt()

        textSizeSeekBar.min = (minTextSize)
        textSizeSeekBar.max = (maxTextSize)
        textSizeSeekBar.progress = initialTextSize.toInt()

        textSizeSeekBar.setOnSeekBarChangeListener(textSizeChangeListener)
    }
}