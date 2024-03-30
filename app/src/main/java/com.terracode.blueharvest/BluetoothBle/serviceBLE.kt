
package com.terracode.blueharvest.BluetoothBle

//import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.onRequestPermissionsResult
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import com.terracode.blueharvest.utils.PreferenceManager


class serviceBLE() : Service() {
    private val binder = LocalBinder()
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BluetoothDevice>
    private var selectedDevice: BluetoothDevice? = null

    // var adapter = BleDeviceAdapter(foundDevices) this should be in activity??
    private lateinit var btManager: BluetoothManager

    override fun onCreate() {
        super.onCreate()
        Log.d("serviBLE", "onCreate LOG!")

        foundDevices = mutableListOf()
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

    fun requestBleScan() {
        Log.d("serviceBle", "Called requestBleScan LOG!")
        initBleScanManager()

        bleScanManager.scanBleDevices()
        // Initialize and start scan here

        // You can inform the activity or show an error message here.

    }

    fun getBtManager(): BluetoothManager {
        return btManager
    }

    fun getFoundDevices(): MutableList<BluetoothDevice> {
        return foundDevices
    }
    fun initBleScanManager() {
        Log.d("serviceBLE", "innit Blescanman!")
        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback(
            {
                val name = it?.device?.address
                if (it != null) {
                    selectedDevice = it.device
                    if (name.isNullOrBlank()) return@BleScanCallback
//                val device = BluetoothDevice //todo this is where I think I can get a whole device, not just name
                    if (!foundDevices.contains(selectedDevice)) {
                        foundDevices.add(it.device)
                        Log.d("ServieBLE_device", getFoundDevices().toString())
                    }
                }
        }))
    }

}
