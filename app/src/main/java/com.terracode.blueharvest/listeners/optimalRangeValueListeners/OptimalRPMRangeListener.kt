package com.terracode.blueharvest.listeners.optimalRangeValueListeners

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

class OptimalRPMRangeListener(
    private val activity: ConfigurationSettingsActivity,
    private val optimalRPMRangeInput: EditText
) :
    TextWatcher {
    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val orangeColor = ContextCompat.getColor(activity, R.color.orange)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.optimalRPMRangeTitle)
    private val maxUserInput = MaxUserInput.MAX_RPM_INPUT.value

    //Current Value
    private val maxRpmDisplayed = PreferenceManager.getMaxRPMDisplayedInput()
    private val currentValue = PreferenceManager.getOptimalRakeRpm()

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
                val value = input.toFloatOrNull()
                value?.let {
                    //If user input > what we defined as a maximum user input
                    val upperRange = currentValue?.plus(it)
                    val lowerRange = currentValue?.minus(it)
                    if (it > maxUserInput){
                        //Make border and text color red
                        optimalRPMRangeInput.setTextColor(redColor)
                        optimalRPMRangeInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputFloatNotification(configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                        //If ranges out of viewing range send notification
                    } else if (upperRange!! > maxRpmDisplayed || lowerRange!! < 0) {
                        //Create the notification for safety value > displayed value
                        val rangeOutOfBoundsNotification = Notifications.rangeOutOfBoundsNotification(configName, it)
                        PreferenceManager.setNotification(rangeOutOfBoundsNotification)
                        //Make border and text color orange
                        optimalRPMRangeInput.setTextColor(orangeColor)
                        optimalRPMRangeInput.setBackgroundResource(R.drawable.edit_text_orange_border)
                        //Create warning toast
                        CustomToasts.safetyValueGreaterThanDisplayedValueToast(activity)
                        //Still save the value
                        PreferenceManager.setOptimalRPMRangeInput(it)

                        //Else, save without notifications/toasts
                    } else {
                        optimalRPMRangeInput.setTextColor(blackColor)
                        optimalRPMRangeInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setOptimalRPMRangeInput(it)
                    }
                }
            }
        }
    }
}
