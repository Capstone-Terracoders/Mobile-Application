package com.terracode.blueharvest.listeners.optimalRangeValueListeners

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

class OptimalHeightRangeListener(
    private val activity: ConfigurationSettingsActivity,
    private val optimalHeightRangeInput: EditText
) :
    TextWatcher {
    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val orangeColor = ContextCompat.getColor(activity, R.color.orange)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.optimalHeightRangeTitle)
    private var maxUserInput = MaxUserInput.MAX_HEIGHT_INPUT.value.toDouble()

    //Current Value
    private val maxHeightDisplayed = PreferenceManager.getMaxHeightDisplayedInput()
    private val unitToggle = PreferenceManager.getSelectedUnit()
    private val currentValue = PreferenceManager.getOptimalRakeRpm()


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
                val value = input.toFloatOrNull()
                value?.let {
                    //If user input > what we defined as a maximum user input
                    val upperRange = currentValue?.plus(it)
                    val lowerRange = currentValue?.minus(it)
                    if (it > maxUserInput){
                        //Make border and text color red
                        optimalHeightRangeInput.setTextColor(redColor)
                        optimalHeightRangeInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueHeightToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputOptimalHeightRangeNotification(configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                        //If ranges out of viewing range send notification
                    } else if (upperRange!! > maxHeightDisplayed || lowerRange!! < 0) {
                        //Create the notification for safety value > displayed value
                        val rangeOutOfBoundsNotification = Notifications.rangeOutOfBoundsNotification(configName, it)
                        PreferenceManager.setNotification(rangeOutOfBoundsNotification)
                        //Make border and text color orange
                        optimalHeightRangeInput.setTextColor(orangeColor)
                        optimalHeightRangeInput.setBackgroundResource(R.drawable.edit_text_orange_border)
                        //Create warning toast
                        CustomToasts.safetyValueGreaterThanDisplayedValueToast(activity)
                        //Still save the value
                        PreferenceManager.setOptimalHeightRangeInput(it)

                        //Else, save without notifications/toasts
                    } else {
                        optimalHeightRangeInput.setTextColor(blackColor)
                        optimalHeightRangeInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setOptimalHeightRangeInput(it)
                    }
                }
            }
        }
    }
}
