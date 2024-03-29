package com.terracode.blueharvest.utils.objects

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.R
@SuppressLint("InflateParams")
object CustomToasts {
    fun maximumValueToast(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.error_toast, null)

        // Find the TextView in the inflated layout
        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = ContextCompat.getString(context, R.string.maximumValueToast)

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
}
