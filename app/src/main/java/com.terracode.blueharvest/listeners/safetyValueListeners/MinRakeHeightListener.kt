package com.terracode.blueharvest.listeners.safetyValueListeners

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.UnitConverter
import com.terracode.blueharvest.utils.constants.MaxUserInputInt
import com.terracode.blueharvest.utils.objects.CustomToasts
import com.terracode.blueharvest.utils.objects.Notifications

class MinRakeHeightListener(
    private val activity: ConfigurationSettingsActivity,
    private val minRakeHeightInput: EditText) :
    TextWatcher {

    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val orangeColor = ContextCompat.getColor(activity, R.color.orange)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.minRakeHeightTitle)
    private var maxUserInput = MaxUserInputInt.MAX_HEIGHT_INPUT.value.toFloat()

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
        //Current maxHeightDisplayed value
        val maxHeightDisplayed = PreferenceManager.getMaxHeightDisplayedInput()
        editable?.let { it ->
            val input = it.toString()
            if (input.isNotEmpty()) {
                val value = input.toIntOrNull()
                value?.let {
                    //If user input > what we defined as a maximum user input
                    if (it > maxUserInput){
                        //Make border and text color red
                        minRakeHeightInput.setTextColor(redColor)
                        minRakeHeightInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueHeightToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputHeightNotification(activity, configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                        //If user input > max rpm displayed
                    } else if (it > maxHeightDisplayed) {
                        //Create the notification for safety value > displayed value
                        val safetyValueGreaterThanDisplayValueNotification = Notifications.safetyValueGreaterThanDisplayValueNotification(activity, configName, it)
                        PreferenceManager.setNotification(safetyValueGreaterThanDisplayValueNotification)
                        //Make border and text color orange
                        minRakeHeightInput.setTextColor(orangeColor)
                        minRakeHeightInput.setBackgroundResource(R.drawable.edit_text_orange_border)
                        //Create warning toast
                        CustomToasts.safetyValueGreaterThanDisplayedValueToast(activity)
                        //Still save the value
                        PreferenceManager.setMinRakeHeightInput(it)

                        //Else, save without notifications/toasts
                    } else {
                        minRakeHeightInput.setTextColor(blackColor)
                        minRakeHeightInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setMinRakeHeightInput(it)
                    }
                }
            }
        }
    }
}
