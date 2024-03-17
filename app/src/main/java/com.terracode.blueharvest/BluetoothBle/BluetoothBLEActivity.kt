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
import com.terracode.blueharvest.BluetoothBle.serviceBLE
import com.terracode.blueharvest.BluetoothBle.serviceBLE.Companion.isServiceBound
import com.terracode.blueharvest.utils.PreferenceManager


class BluetoothBLEActivity : ComponentActivity() {
    private var serviceBLEBound = false
    private var serviceBLE: serviceBLE? = null
    private lateinit var btnStartScan: Button
    private lateinit var btManager: BluetoothManager
    private lateinit var foundDevices: MutableList<BleDevice>
    private lateinit var adapter: BleDeviceAdapter

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_ble)

        // Initialize RecyclerView and adapter
        val rvFoundDevices = findViewById<View>(R.id.rv_found_devices) as RecyclerView
        foundDevices = BleDevice.createBleDevicesList()
        adapter = BleDeviceAdapter(foundDevices)
        rvFoundDevices.adapter = adapter
        rvFoundDevices.layoutManager = LinearLayoutManager(this)

        // Bind the service
        serviceBLE?.let { service ->
            val serviceIntent = Intent(this, service::class.java)
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
        }

        // Reference Kezies preference manager
        PreferenceManager.init(this)

        // Initialize BluetoothManager
        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        // Initialize the start scan button
        btnStartScan = findViewById(R.id.btn_start_scan)
        btnStartScan.setOnClickListener {
            handleStartScanButtonClick()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun handleStartScanButtonClick() {
        when (PermissionsUtilities.checkPermissionsGranted(
            this,
            BleScanRequiredPermissions.permissions
        )) {
            true -> {
                if (serviceBLEBound) {
                    serviceBLE?.requestBleScan(adapter)
                }
            }

            false -> PermissionsUtilities.checkPermissions(
                this, BleScanRequiredPermissions.permissions, BLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as serviceBLE.LocalBinder
            serviceBLE = binder.service
            serviceBLEBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            serviceBLEBound = false
            serviceBLE = null
        }
    }

    override fun onStop() {
        super.onStop()
        if (serviceBLEBound) {
            unbindService(connection)
            serviceBLEBound = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        dispatchOnRequestPermissionsResult(
            requestCode,
            grantResults,
            onGrantedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { serviceBLE?.requestBleScan(adapter) }),
            onDeniedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { Toast.makeText(this,
                "Some permissions were not granted, please grant them and try again",
                Toast.LENGTH_LONG).show() })
        )
    }

    companion object {
        private const val BLE_PERMISSION_REQUEST_CODE = 1
    }
}
