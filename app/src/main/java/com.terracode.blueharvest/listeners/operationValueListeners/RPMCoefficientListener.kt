package com.terracode.blueharvest.listeners.operationValueListeners

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

class RPMCoefficientListener(
    private val activity: ConfigurationSettingsActivity,
    private val rpmCoefficientInput: EditText
) :
    TextWatcher {
    //Colors
    private val redColor = ContextCompat.getColor(activity, R.color.red)
    private val blackColor = ContextCompat.getColor(activity, R.color.black)

    //Constants
    private val configName = ContextCompat.getString(activity, R.string.coefficientRPMTitle)
    private val maxUserInput = MaxUserInputInt.MAX_DEFAULT_INPUT.value

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
                        rpmCoefficientInput.setTextColor(redColor)
                        rpmCoefficientInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueRpmAndCoefficientToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputDefaultNotification(activity, configName, it)
                        PreferenceManager.setNotification(maxValueNotification)

                    //Else, save value
                    } else {
                        rpmCoefficientInput.setTextColor(blackColor)
                        rpmCoefficientInput.setBackgroundResource(R.drawable.edit_text_normal)
                        PreferenceManager.setRPMCoefficientInput(it)
                    }
                }
            }
        }
    }
}
