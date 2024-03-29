package com.terracode.blueharvest.BluetoothBle

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
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.dispatchOnRequestPermissionsResult
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager


class BluetoothBLEActivity : ComponentActivity() {
    //private var serviceBLEBound = false

    private lateinit var btManager: BluetoothManager
    private lateinit var foundDevices: MutableList<BleDevice>
    private lateinit var adapter: BleDeviceAdapter
    private lateinit var myBLEService: serviceBLE
    private var myBLEBound: Boolean = false



    private lateinit var btnStartScan: Button
    private lateinit var btnConnectDevice: Button

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BluetoothBLEActivity", "oncreate LOG!");

        setContentView(R.layout.activity_bluetooth_ble)


        startService(Intent(this@BluetoothBLEActivity, serviceBLE::class.java))

        val serviceIntent = Intent(this@BluetoothBLEActivity, serviceBLE::class.java)
        Log.d("BluetoothBLEActivity", "Attempting to bind to serviceBLE")
        //bind the service
        val bluetoothService = bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        // Initialize RecyclerView and adapter
        val rvFoundDevices = findViewById<View>(R.id.rv_found_devices) as RecyclerView
        foundDevices = BleDevice.createBleDevicesList()
        adapter = BleDeviceAdapter(foundDevices)

        rvFoundDevices.adapter = adapter
        rvFoundDevices.layoutManager = LinearLayoutManager(this)





        //initialize bt manager
        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        // Initialize the start scan button
        btnStartScan = findViewById(R.id.btn_start_scan)
        btnStartScan.setOnClickListener {
            handleStartScanButtonClick()
        }



    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun handleStartScanButtonClick() {
      /*  when (PermissionsUtilities.checkPermissionsGranted(
            this,
            BleScanRequiredPermissions.permissions
        )) {
            true -> {
                Log.d("BluetoothBLEActivity", "handle start scan button true LOG!");
                if (serviceBLEBound) {
                    myBLEService.requestBleScan(adapter)
                }
            }

            false -> PermissionsUtilities.checkPermissions(

                this, BleScanRequiredPermissions.permissions, BLE_PERMISSION_REQUEST_CODE
            )

        }*/
        Log.d("BluetoothBLEActivity", "handle start scan button LOG!");
        if (myBLEBound) {
            Log.d("BluetoothBLEActivity", "handle start scan button true LOG!");
            myBLEService.requestBleScan(adapter)
        }

    }


    private val connection = object : ServiceConnection {
        //is called on service bind, idk how android magic I think
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as serviceBLE.LocalBinder
            myBLEService = binder.getService()
            myBLEBound = true
            Log.d("BluetoothBLEActivity", "connection ");

            val foundDevices = myBLEService.getFoundDevices()
            val adapter = BleDeviceAdapter(foundDevices)

        }

        override fun onServiceDisconnected(className: ComponentName) {

            myBLEBound = false
        }
    }

    override fun onStop() {
        super.onStop()
        //I dont think I want to unbind the service here,
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        dispatchOnRequestPermissionsResult(
            requestCode,
            grantResults,
            onGrantedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { myBLEService.requestBleScan(adapter) }),
            onDeniedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { Toast.makeText(this,
                "Some permissions were not granted, please grant them and try again",
                Toast.LENGTH_LONG).show() })
        )
    }

    companion object {
        private const val BLE_PERMISSION_REQUEST_CODE = 1
    }
}
