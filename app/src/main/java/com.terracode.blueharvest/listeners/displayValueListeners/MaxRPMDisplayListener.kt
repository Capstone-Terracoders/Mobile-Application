package com.terracode.blueharvest.listeners.displayValueListeners

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

class MaxRPMDisplayListener(
    private val activity: ConfigurationSettingsActivity,
    private val maxRPMDisplayedInput: EditText
) :
    TextWatcher {

    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val orangeColor = ContextCompat.getColor(activity, R.color.orange)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.maxRPMDisplayedTitle)
    private val maxUserInput = MaxUserInput.MAX_RPM_INPUT.value


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
                        maxRPMDisplayedInput.setTextColor(redColor)
                        maxRPMDisplayedInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueRpmAndCoefficientToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputRpmNotification(configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                    //Else if value is not divisible by 5
                    } else if (it % 5 != 0){
                        val notDivisibleByFiveNotification = Notifications.notDivisibleByFiveNotification(configName, it)
                        PreferenceManager.setNotification(notDivisibleByFiveNotification)
                        //Make border and text color orange
                        maxRPMDisplayedInput.setTextColor(orangeColor)
                        maxRPMDisplayedInput.setBackgroundResource(R.drawable.edit_text_orange_border)
                        //Create warning toast
                        CustomToasts.notDivisibleByFiveNotification(activity)
                        //Still save the value
                        PreferenceManager.setMaxRPMDisplayedInput(it)
                    } else {
                        maxRPMDisplayedInput.setTextColor(blackColor)
                        maxRPMDisplayedInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setMaxRPMDisplayedInput(it)
                    }
                }
            }
        }
    }
}