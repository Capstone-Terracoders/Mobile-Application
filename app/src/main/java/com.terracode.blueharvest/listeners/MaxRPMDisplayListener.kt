package com.terracode.blueharvest.listeners

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.utils.PreferenceManager

class MaxRPMDisplayListener (private val activity: ConfigurationSettingsActivity) :
   TextWatcher {
    private var rpmDisplayedEditText: EditText? = null
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        PreferenceManager.init(activity)
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val rpmDisplayedValue = rpmDisplayedEditText!!.text.toString()
        setPreferenceManagerValue(rpmDisplayedValue)
    }

    override fun afterTextChanged(p0: Editable?) {
        activity.recreate()
    }

    private fun setPreferenceManagerValue(rpmDisplayedValue: String) {
        PreferenceManager.setMaxRPMDisplayedInput(rpmDisplayedValue)
    }
}