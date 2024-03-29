package com.terracode.blueharvest.listeners.safetyValueListeners

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.constants.MaxUserInput
import com.terracode.blueharvest.utils.objects.CustomToasts
import com.terracode.blueharvest.utils.objects.Notifications

class MaxRakeRPMListener(
    private val activity: ConfigurationSettingsActivity,
    private val maxRakeRPMInput: EditText) :
    TextWatcher {

        //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val orangeColor = ContextCompat.getColor(activity, R.color.orange)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.maxRakeRPMTitle)
    private val maxUserInput = MaxUserInput.MAX_INPUT.value

    //Current Value
    private val maxRpmDisplayed = PreferenceManager.getMaxRPMDisplayedInput()

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        PreferenceManager.init(activity)
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
                        maxRakeRPMInput.setTextColor(redColor)
                        maxRakeRPMInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputNotification(configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                        //If user input > max rpm displayed
                    } else if (it > maxRpmDisplayed) {
                        //Create the notification for safety value > displayed value
                        val safetyValueGreaterThanDisplayValueNotification = Notifications.safetyValueGreaterThanDisplayValueNotification(configName, it)
                        PreferenceManager.setNotification(safetyValueGreaterThanDisplayValueNotification)
                        //Make border and text color orange
                        maxRakeRPMInput.setTextColor(orangeColor)
                        maxRakeRPMInput.setBackgroundResource(R.drawable.edit_text_orange_border)
                        //Create warning toast
                        CustomToasts.safetyValueGreaterThanDisplayedValueToast(activity)
                        //Still save the value
                        PreferenceManager.setMaxRakeRPMInput(it)

                        //Else, save without notifications/toasts
                    } else {
                        maxRakeRPMInput.setTextColor(blackColor)
                        maxRakeRPMInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setMaxRakeRPMInput(it)
                    }
                }
            }
        }
    }
}
