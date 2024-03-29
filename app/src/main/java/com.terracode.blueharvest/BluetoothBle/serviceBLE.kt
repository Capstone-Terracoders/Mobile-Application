
package com.terracode.blueharvest.BluetoothBle

import android.app.Service
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.BluetoothBle.BleDevice
import com.terracode.blueharvest.BluetoothBle.BleDeviceAdapter
import com.terracode.blueharvest.BluetoothBle.BleScanCallback
import com.terracode.blueharvest.BluetoothBle.BleScanManager
import com.terracode.blueharvest.BluetoothBle.BleScanRequiredPermissions
import com.terracode.blueharvest.BluetoothBle.BluetoothBLEActivity
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.checkPermissionsGranted
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.dispatchOnRequestPermissionsResult
//import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.onRequestPermissionsResult
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager


class serviceBLE() : Service() {


    private val binder = LocalBinder()
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BleDevice>

    // var adapter = BleDeviceAdapter(foundDevices) this should be in activity??
    private lateinit var btManager: BluetoothManager

    override fun onCreate() {
        super.onCreate()
        Log.d("serviBLE", "onCreate LOG!")

        foundDevices = BleDevice.createBleDevicesList()
        // adapter = BleDeviceAdapter(foundDevices) this should be in activity??
        btManager = getSystemService(BluetoothManager::class.java)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("serviBLE", "onStartCommand!")

        return START_STICKY // If the service is killed, it will be automatically restarted
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): serviceBLE = this@serviceBLE

    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("serviBLE", "onBind LOG!")
       return binder
    }

    /*
    private fun performLongTask() {
        // Imagine doing something that takes a long time here
        Log.d("serviBLE", "preformLongTask LOG!")
        Thread.sleep(5000)
    }*/

    override fun onDestroy() {
        super.onDestroy()//make sure to unbind from activity
        Log.d("serviBLE", "onDestroy LOG!")
    }

    fun requestBleScan(adapter: BleDeviceAdapter) {
        Log.d("RequestBLEScan", "Call LOG!")
        initBleScanManager(adapter)
        bleScanManager.scanBleDevices()
        // Initialize and start scan here

        // You can inform the activity or show an error message here.

    }

    fun getFoundDevices(): MutableList<BleDevice> {
        return foundDevices
    }

    fun getBtManager(): BluetoothManager {
        return btManager
    }

    fun initBleScanManager(adapter: BleDeviceAdapter) {


        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback({
            val name = it?.device?.address
            if (name.isNullOrBlank()) return@BleScanCallback
            // val adapter = BleDeviceAdapter(foundDevices)
            val device = BleDevice(name)
            if (!foundDevices.contains(device)) {
                foundDevices.add(device)
                adapter.notifyItemInserted(foundDevices.size - 1)
            }
        }))
    }
}
