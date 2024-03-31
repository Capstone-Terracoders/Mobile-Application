package com.terracode.blueharvest.listeners.operationValueListeners

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

class HeightCoefficientListener(
    private val activity: ConfigurationSettingsActivity,
    private val heightCoefficientInput: EditText
) :
    TextWatcher {
    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.coefficientHeightTitle)
    private var maxUserInput = MaxUserInput.MAX_RPM_INPUT.value.toDouble()

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
                        heightCoefficientInput.setTextColor(redColor)
                        heightCoefficientInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueRpmAndCoefficientToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputCoefficientNotification(configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                    //Else, save value
                    } else {
                        heightCoefficientInput.setTextColor(blackColor)
                        heightCoefficientInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setHeightCoefficientInput(it)
                    }
                }
            }
        }
    }
}
