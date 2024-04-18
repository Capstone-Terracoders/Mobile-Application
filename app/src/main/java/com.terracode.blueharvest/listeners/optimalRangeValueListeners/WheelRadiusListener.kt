package com.terracode.blueharvest.listeners.optimalRangeValueListeners

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.constants.MaxUserInputInt
import com.terracode.blueharvest.utils.objects.CustomToasts
import com.terracode.blueharvest.utils.objects.Notifications

class WheelRadiusListener(
    private val activity: ConfigurationSettingsActivity,
    private val wheelRadiusInput: EditText
) :
    TextWatcher {
    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.wheelRadiusTitle)
    private var maxUserInputImperial = MaxUserInputInt.MAX_WHEEL_INPUT_IMPERIAL.value.toFloat()
    private var maxUserInputMetric = MaxUserInputInt.MAX_WHEEL_INPUT_METRIC.value.toFloat()
    private var toggle = PreferenceManager.getSelectedUnit()

    //Variable to determine which unit constant to use:
    private var maxInput = 0f

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        PreferenceManager.init(activity)
        maxInput = if (toggle){
            maxUserInputMetric
        } else {
            maxUserInputImperial
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
                    if (it > maxInput){
                        //Make border and text color red
                        wheelRadiusInput.setTextColor(redColor)
                        wheelRadiusInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueWheelToast(activity, toggle)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxWheelNotificationFloat(activity, configName, it, toggle)
                        PreferenceManager.setNotification(maxValueNotification)

                        //Else, save without notifications/toasts
                    } else {
                        wheelRadiusInput.setTextColor(blackColor)
                        wheelRadiusInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setWheelRadiusInput(it)
                    }
                }
            }
        }
    }
}
