package com.terracode.blueharvest.BluetoothBle

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.terracode.blueharvest.R

// Adapter for displaying a list of Bluetooth LE devices in a RecyclerView
class BleDeviceAdapter (private val devices: List<BluetoothDevice>) : RecyclerView.Adapter<BleDeviceAdapter.ViewHolder>() {
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val deviceNameTextView: TextView = itemView.findViewById<TextView>(R.id.device_name)
//    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.device_name)
        }
    }

    // Called when a new view holder is needed to display a device
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val deviceView = inflater.inflate(R.layout.device_row_layout, parent, false)
        return ViewHolder(deviceView)
    }
    // Called to bind data to an existing view holder for a specific position
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        val textView = holder.textView
        textView.text = device.address
        Log.d("BLeDeviceAddapter", "Device"+textView.text)
        holder.textView.setOnClickListener(View.OnClickListener() {
            Log.d("BLeDeviceAddapter", "Connecting to device...."+textView.text)
        })
    }
    // Returns the total number of devices in the list
    override fun getItemCount(): Int {
        return devices.size
    }
}