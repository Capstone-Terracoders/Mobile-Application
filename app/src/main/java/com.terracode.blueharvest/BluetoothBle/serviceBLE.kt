
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
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.PreferenceManager
import kotlinx.coroutines.selects.select
import org.json.JSONObject
import java.nio.ByteBuffer
import java.util.UUID//
//formats for incoming characteristic values are as follows:
//{"OptRakeHeight": #,"OptRakeRPM": #}
//{"RPM": , "Rake Height": , "Bush Height": , "Speed": }
//{"Raw RPM": , "Raw Rake Height": , "Raw Bush Height": , "Raw Speed": }
//post processing current height



val sensorDataCharacteristicUUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
//pre prosessing
val sensorRawDataCharacteristicUUID = UUID.fromString("0000c0de-0000-1000-8000-00805f9b34fb")
//config is what we are sending back
val configurationCharacteristicUUID = UUID.fromString("0000beef-0000-1000-8000-00805f9b34fb")
//height and rpm
val optimalOperationCharacteristicUUID = UUID.fromString("0000fade-0000-1000-8000-00805f9b34fb")

class serviceBLE() : Service() {
    private val binder = LocalBinder()
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BluetoothDevice>
    private var selectedDevice: BluetoothDevice? = null
    private var selectedCharacteristic: BluetoothGattCharacteristic? = null
    //Our connection to the selected device
    private var gatt: BluetoothGatt? = null
    private lateinit var characteristics: MutableList<BluetoothGattCharacteristic>
    private lateinit var btManager: BluetoothManager
    private val GATT_MAX_MTU_SIZE = 517


    private var readListener: ReadListener? = null
    interface ReadListener {
        fun onValueObtained(characteristic: BluetoothGattCharacteristic, value: ByteArray)
    }

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
    fun getFoundDevices(): MutableList<BluetoothDevice> {
        return foundDevices
    }


    fun getGatt(): BluetoothGatt? {
        return gatt
    }


fun getSelectedCharacteristic() : BluetoothGattCharacteristic? {
    return selectedCharacteristic
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


        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            Log.d("alex log", "\"ATT MTU changed to $mtu, success: ${status == BluetoothGatt.GATT_SUCCESS}")
        }

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            //This tells us when we're connected or disconnected from the peripheral.

            if (status != BluetoothGatt.GATT_SUCCESS) {
                //TODO: handle error

            }
            if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                // Handle disconnected state
                Log.d("alex log", "Disconnected from ${selectedDevice?.address}")
                // Attempt to reconnect or take other actions
                //fatal disconnect screen
            }
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                //TODO: handle the fact that we've just connected
                Log.d("alex log", " serviceBLE successful BLE Connection")
                gatt.discoverServices()



             //   gatt.requestMtu(GATT_MAX_MTU_SIZE)
                Log.d("alex log", " serviceBLE discover, request MTU")


            }
            return
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val services = gatt?.services                          //See if the service discovery was successful
                Log.d("alex log ", "service BLE OSD servicessssssss: $services")
              // val service =
              //  for service in services
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val discoveredServices = gatt?.services  // Get all discovered services
                    if (discoveredServices != null) {
                        for (service in discoveredServices) {
                            // Iterate through each service
                            val characteristics = service.getCharacteristics()  // Get characteristics of current service

                            if (characteristics != null) {
                                for (characteristic in characteristics) {
                                    // Iterate through each characteristic
                                    enableNotifications(characteristic)// Enable notifications for this characteristic
                                    Log.d("alex log ", "notifications enabled for  $characteristic")
                                }
                            }
                        }
                    }
                }

            }
        }

        fun parseByteArray(data: ByteArray): List<Float> {
            val decodedString = String(data, Charsets.UTF_8) // Decode bytes to string
            val values = decodedString.trim { it <= ' ' }.split(",") // Split by comma, trim whitespaces

            val floatList = mutableListOf<Float>()
            for (value in values) {
                try {
                    floatList.add(value.toFloat()) // Convert each value to float
                } catch (e: NumberFormatException) {
                    Log.w("FloatExtraction", "Error converting value '$value' to float", e)
                }
            }
            return floatList
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic) {
                Log.i("BluetoothGattCallback", "Characteristic $uuid changed | value: ${value}")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
//            if (characteristic.uuid == myCharacteristicUUID) {
            Log.v("alex log", characteristic.uuid.toString()+" from onCharacteristic Read")
            val data = characteristic.value
            val parsedValue = data[0].toInt()
           // val parsedValue = parseByteArray( data )
            Log.d("alex log", " from onCharread int  $parsedValue")

            selectedCharacteristic = characteristic
            readListener?.onValueObtained(characteristic, value)

        }
        fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

        fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
            containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

        fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean =
            properties and property != 0

        @SuppressLint("MissingPermission")
        fun enableNotifications(characteristic: BluetoothGattCharacteristic) {
            //val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
            val payload = when {
                characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                else -> {
                    Log.e("ConnectionManager", "${characteristic.uuid} doesn't support notifications/indications")
                    return
                }
            }
//            characteristic.getDescriptor(cccdUuid)?.let { cccDescriptor ->
//                if (gatt?.setCharacteristicNotification(characteristic, true) == false) {
//                    Log.e("ConnectionManager", "setCharacteristicNotification failed for ${characteristic.uuid}")
//                    return
//                }
//                writeDescriptor(cccDescriptor, payload)
//        } ?: Log.e("ConnectionManager", "${characteristic.uuid} doesn't contain the CCC descriptor!")
        }
        @SuppressLint("MissingPermission")
        fun writeDescriptor(descriptor: BluetoothGattDescriptor, payload: ByteArray) {
            gatt?.let { gatt ->
                descriptor.value = payload
                gatt.writeDescriptor(descriptor)
            } ?: error("Not connected to a BLE device!")
        }
    }

@SuppressLint("MissingPermission")
fun readCharacteristic(
    characteristic: BluetoothGattCharacteristic?,
    readListener: ReadListener
): Boolean {
    if (gatt == null || characteristic == null) return false

    // Register the listener
    this.readListener = readListener

    // Trigger the read operation
    return gatt?.readCharacteristic(characteristic) ?: false
}


}
