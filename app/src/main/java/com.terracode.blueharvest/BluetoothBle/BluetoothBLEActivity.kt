package com.terracode.blueharvest.BluetoothBle

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.dispatchOnRequestPermissionsResult
import com.terracode.blueharvest.R
import serviceBLE



class BluetoothBLEActivity : ComponentActivity() {
    private var serviceBLEBound = false
    private var serviceBLE: serviceBLE? = null
    private lateinit var btnStartScan: Button
   // private lateinit var btManager: BluetoothManager
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BleDevice>//list of found devices

    @SuppressLint("NotifyDataSetChanged", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_ble)

        // RecyclerView handling
        val rvFoundDevices = findViewById<View>(R.id.rv_found_devices) as RecyclerView
        foundDevices = BleDevice.createBleDevicesList()//returns mutablelistog
        val adapter = BleDeviceAdapter(foundDevices)
        rvFoundDevices.adapter = adapter
        rvFoundDevices.layoutManager = LinearLayoutManager(this)

        // BleManager creation


        // Adding the actions the manager must do before and after scanning
        bleScanManager.beforeScanActions.add { btnStartScan.isEnabled = false }
        bleScanManager.beforeScanActions.add {
            foundDevices.clear()
            adapter.notifyDataSetChanged()
        }


        bleScanManager.afterScanActions.add { btnStartScan.isEnabled = true }

        // Adding the onclick listener to the start scan button
        btnStartScan = findViewById(R.id.btn_start_scan)
        btnStartScan.setOnClickListener {
            // Checks if the required permissions are granted and starts the scan if so, otherwise it requests them
            when (PermissionsUtilities.checkPermissionsGranted(this, BleScanRequiredPermissions.permissions))
            {
                true -> {
                    //check if bound to btservice
                    if (/*serviceBLE.isBound */ true == true) {// TODO:  
                      //  serviceBLE?.scanBleDevices()
                    }
                }
                false -> PermissionsUtilities.checkPermissions(
                    this, BleScanRequiredPermissions.permissions, BLE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
      //todo      serviceBLE = (service as serviceBLE.LocalBinder).service
            serviceBLEBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            serviceBLEBound = false
            serviceBLE = null
        }
    }
    override fun onStart() {
        super.onStart()
        Intent(this, serviceBLE!!::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }
    override fun onStop() {
        super.onStop()
        if (serviceBLEBound) {
            unbindService(connection)
            serviceBLEBound = false
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        dispatchOnRequestPermissionsResult(
            requestCode,
            grantResults,
            onGrantedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { bleScanManager.scanBleDevices() }),
            onDeniedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { Toast.makeText(this,
                "Some permissions were not granted, please grant them and try again",
                Toast.LENGTH_LONG).show() })
        )
    }

    companion object { private const val BLE_PERMISSION_REQUEST_CODE = 1 }
}
