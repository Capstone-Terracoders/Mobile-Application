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

    private val redColor = ContextCompat.getColor(activity, R.color.red)

    private val configName = ContextCompat.getString(activity, R.string.maxRakeRPMTitle)
    private val maxRpmUserInput = MaxUserInput.MAX_RPM_DISPLAYED.value

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
                    if (it > maxRpmUserInput){
                        //Make border and text color red
                        maxRakeRPMInput.setTextColor(redColor)
                        maxRakeRPMInput.setBackgroundResource(R.drawable.edit_text_red_border)
                        //Create warning toast
                        CustomToasts.maximumValueToast(activity)
                        //Create notification
                        val maxValueNotification = Notifications.getMaxInputNotification(configName, value)
                        PreferenceManager.setNotification(maxValueNotification)
                    } else {
                        //Else, save value
                        PreferenceManager.setMaxRakeRPMInput(it)
                    }
                }
            }
        }
    }
}
