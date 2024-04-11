package com.terracode.blueharvest.utils.objects

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.constants.MaxUserInputString

@Suppress("DEPRECATION")
@SuppressLint("InflateParams")
object CustomToasts {
    private val defaultMaxInput = MaxUserInputString.MAX_DEFAULT_INPUT.value
    private val heightMaxInput = MaxUserInputString.MAX_HEIGHT_INPUT.value
    fun maximumValueRpmAndCoefficientToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.error_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        val toastText = context.getString(
            R.string.maximumValueToast,
            defaultMaxInput
        )
        textView.text = toastText

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun maximumValueHeightToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.error_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        val toastText = context.getString(
            R.string.maximumValueToast,
            heightMaxInput
        )
        textView.text = toastText

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun safetyValueGreaterThanDisplayedValueToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.warning_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.safetyValueGreaterThanMaximumDisplayedValueToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun rangeOutOfBoundsToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.warning_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.rangeOutOfBoundsToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun notDivisibleByFiveToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.warning_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.notDivisibleByFiveToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun inputBelowFiveToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.warning_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.inputBelowFiveToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun displayedValueLessThanSafetyValueToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.warning_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.displayedValueLessThanSafetyValueToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    //Toasts for bluetooth device connection
    fun bluetoothDeviceConnectedToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.normal_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.bluetoothDeviceConnectedNotificationToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }

    fun bluetoothDeviceDisconnectedToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.normal_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.bluetoothDeviceDisconnectedNotificationToast)

        with(Toast(context)) {
            // Set custom layout to the Toast's view
            view = layout
            duration = Toast.LENGTH_LONG
            show()
        }
    }
}