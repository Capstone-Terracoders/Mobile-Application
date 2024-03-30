
package com.terracode.blueharvest.BluetoothBle

//import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.onRequestPermissionsResult
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
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

    fun requestBleScan(adapter: BleDeviceAdapter, activity: Activity) {
        Log.d("serviceBle", "Called requestBleScan LOG!")
        initBleScanManager(adapter, activity)

        bleScanManager.scanBleDevices()
        // Initialize and start scan here

        // You can inform the activity or show an error message here.

    }

    fun getBtManager(): BluetoothManager {
        return btManager
    }

    fun getFoundDevices(): MutableList<BleDevice> {
        Log.d("FoundDevices RETURNED", foundDevices.toString())
        return foundDevices
    }

    @SuppressLint("SuspiciousIndentation")
    fun initBleScanManager(adapter: BleDeviceAdapter, activity: Activity) {
        PreferenceManager.init(activity)
        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback(
            {
                val name = it?.device?.address
                    if (name.isNullOrBlank()) return@BleScanCallback
                    //val adapter = BleDeviceAdapter(foundDevices)
                val device = BleDevice(name)//todo this is where I think I can get a whole device, not just name
                    if (!foundDevices.contains(device)) {
                        foundDevices.add(device)
                        Log.d("FoundDevices-01", foundDevices.toString())
                        adapter.notifyItemInserted(foundDevices.size - 1)
            }
            }))
        Log.d("FoundDevices-02", foundDevices.toString())
        PreferenceManager.setFoundDevices(foundDevices)
    }
}
