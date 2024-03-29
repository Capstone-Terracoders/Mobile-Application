package com.terracode.blueharvest.listeners.displayValueListeners

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.UnitConverter
import com.terracode.blueharvest.utils.constants.MaxUserInput
import com.terracode.blueharvest.utils.objects.CustomToasts
import com.terracode.blueharvest.utils.objects.Notifications

class MaxHeightDisplayListener(
    private val activity: ConfigurationSettingsActivity,
    private val maxHeightDisplayedInput: EditText
) :
    TextWatcher {

    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.maxHeightDisplayedTitle)
    private var maxUserInput = MaxUserInput.MAX_HEIGHT_INPUT.value.toDouble()

    //Current Value
    private val unitToggle = PreferenceManager.getSelectedUnit()

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        PreferenceManager.init(activity)
        if (!unitToggle){
            maxUserInput = UnitConverter.convertHeightToImperial(maxUserInput)!!
        }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No action needed during text change
    }

    override fun afterTextChanged(editable: Editable?) {
        editable?.let { it ->
            val input = it.toString()
            if (input.isNotEmpty()) {
                val value = input.toIntOrNull()
                value?.let {
                    //If user input > what we defined as a maximum user input
                    if (it > maxUserInput){
                        //Make border and text color red
                        maxHeightDisplayedInput.setTextColor(redColor)
                        maxHeightDisplayedInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputNotification(configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                    //Else, save value
                    } else {
                        maxHeightDisplayedInput.setTextColor(blackColor)
                        maxHeightDisplayedInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setMaxHeightDisplayedInput(it)
                    }
                }
            }
        }
    }
}
