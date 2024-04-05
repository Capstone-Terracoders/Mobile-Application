
package com.terracode.blueharvest.BluetoothBle

//import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.onRequestPermissionsResult
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import java.util.UUID


class serviceBLE() : Service() {
    private val binder = LocalBinder()
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BluetoothDevice>
    private var selectedDevice: BluetoothDevice? = null
    private var selectedCharacteristic: BluetoothGattCharacteristic? = null
    //Our connection to the selected device
    private var gatt: BluetoothGatt? = null

    private lateinit var btManager: BluetoothManager

    override fun onCreate() {
        super.onCreate()
        Log.d("alex log", " serviceBLE onCreate LOG!")
        foundDevices = mutableListOf()
        btManager = getSystemService(BluetoothManager::class.java)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("alex log", " serviceBLE onStartCommand!")

        return START_STICKY // If the service is killed, it will be automatically restarted
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): serviceBLE = this@serviceBLE

    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("alex log", " serviceBLE onBind LOG!")
       return binder
    }


    override fun onDestroy() {
        super.onDestroy()//make sure to unbind from activity
        Log.d("alex log", " serviceBLE onDestroy LOG!")
    }

    fun requestBleScan() {
        Log.d("alex log", " serviceBLE Called requestBleScan LOG!")
        initBleScanManager()

        bleScanManager.scanBleDevices()
        // Initialize and start scan here
    }

    fun getBtManager(): BluetoothManager {
        return btManager
    }

    fun getFoundDevices(): MutableList<BluetoothDevice> {
        return foundDevices
    }
    @SuppressLint("MissingPermission")
    fun initBleScanManager() {
        Log.d("alex log", " serviceBLE innit Blescanman!")
        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback(
            {
                val name = it?.device?.address
                if (it != null) {
                    selectedDevice = it.device
                    if (name.isNullOrBlank()) return@BleScanCallback
//                val device = BluetoothDevice //todo this is where I think I can get a whole device, not just name
                    if (!foundDevices.contains(selectedDevice)) {
                        foundDevices.add(it.device)
                        Log.d("alex log", getFoundDevices().toString())
                    }
                }
        }))
    }

fun getSelectedCharacteristic() : BluetoothGattCharacteristic? {
    return selectedCharacteristic
}
fun getSelectedDevice() : BluetoothDevice? {
    return selectedDevice
}
fun setSelectedDevice(device: BluetoothDevice){
    selectedDevice = device
    Log.d("alex log", " serviceBLE Set Device $device")
}
    @SuppressLint("MissingPermission")
fun connectToDevice(context: Context){
    gatt = selectedDevice!!.connectGatt(context, false, callback)
}
    //Whatever we do with our Bluetooth device connection, whether now or later, we will get the
    //results in this callback object, which can become massive.
    private val callback = object: BluetoothGattCallback() {
        //We will override more methods here as we add functionality.
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            //This tells us when we're connected or disconnected from the peripheral.

            if (status != BluetoothGatt.GATT_SUCCESS) {
                //TODO: handle error
                return
            }

            if (newState == BluetoothGatt.STATE_CONNECTED) {
                //TODO: handle the fact that we've just connected
                Log.d("alex log", " serviceBLE successful BLE Connection")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
//            if (characteristic.uuid == myCharacteristicUUID) {
            Log.v("bluetooth", characteristic.uuid.toString())
            selectedCharacteristic = characteristic
//            }
        }
    }
//    @RequiresPermission(PERMISSION_BLUETOOTH_CONNECT)
    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (selectedDevice == null || gatt == null) {                      //Check that we have access to a Bluetooth radio
            return
        }
        val status =
            gatt!!.readCharacteristic(characteristic)                              //Request the BluetoothGatt to Read the characteristic
        Log.i("Alex log", "READ STATUS $status")
    }
}
