
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
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import com.terracode.blueharvest.utils.PreferenceManager
import kotlinx.coroutines.selects.select
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
        PreferenceManager.setMyBleStarted(true)
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
        Log.d("alex log", " serviceBLE onDestroy LOG!")
        PreferenceManager.setMyBleStarted(false)
        super.onDestroy()//make sure to unbind from activity


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
    gatt = selectedDevice!!.connectGatt(context, false, gattCallback)
}
    //Whatever we do with our Bluetooth device connection, whether now or later, we will get the
    //results in this callback object, which can become massive.
    private val gattCallback = object: BluetoothGattCallback() {
        //We will override more methods here as we add functionality.
        val deviceAddress = gatt?.device?.address


        @SuppressLint("MissingPermission")
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
                gatt.discoverServices()


            }
           if(newState == BluetoothGatt.STATE_DISCONNECTED) {
                // Handle disconnected state
                Log.d("ServiceBLE", "Disconnected from ${selectedDevice?.address}")
                // Attempt to reconnect or take other actions
               //fatal disconnect screen
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {                                 //See if the service discovery was successful
                Log.d("alex log", "start gat service discovery **$status")

            } else {                                                                      //Service discovery failed so log a warning
                Log.d("alex log", "onServicesDiscovered received: $status")
            }
          //  printGattTable()
        }
//        private fun printGattTable() {
//            if (services.isEmpty()) {
//                Log.i("printGattTable", "No service and characteristic available, call discoverServices() first?")
//                return
//            }
//            services.forEach { service ->
//                val characteristicsTable = service.characteristics.joinToString(
//                    separator = "\n|--",
//                    prefix = "|--"
//                ) { it.uuid.toString() }
//                Log.i("printGattTable", "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
//                )
//            }
//        }

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

@SuppressLint("MissingPermission")
fun readCharacteristic(serviceUUID: UUID, characteristicUUID: UUID) {
        val service = gatt?.getService(serviceUUID)
        val characteristic = service?.getCharacteristic(characteristicUUID)

        if (characteristic != null) {
            val success = gatt?.readCharacteristic(characteristic)
            Log.d("alex log", " serviceBLE Read status: $success")
        }
    }
     private fun BluetoothGatt.printGattTable() {
        if (services.isEmpty()) {
            Log.i("printGattTable", "No service and characteristic available, call discoverServices() first?")
            return
        }
        services.forEach { service ->
            val characteristicsTable = service.characteristics.joinToString(
                separator = "\n|--",
                prefix = "|--"
            ) { it.uuid.toString() }
            Log.i("printGattTable", "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
            )
        }}

}
