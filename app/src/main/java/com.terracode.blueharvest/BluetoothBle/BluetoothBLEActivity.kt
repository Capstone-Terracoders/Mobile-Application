package com.terracode.blueharvest.BluetoothBle

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terracode.blueharvest.AccessibilitySettingsActivity
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.dispatchOnRequestPermissionsResult
import com.terracode.blueharvest.ConfigurationSettingsActivity
import com.terracode.blueharvest.R
import com.terracode.blueharvest.services.toolbarServices.BackButtonService
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.NotificationManager


class BluetoothBLEActivity : BleDeviceAdapter.ItemClickListener, AppCompatActivity() {


    private lateinit var btManager: BluetoothManager
    private lateinit var foundDevices: MutableList<BluetoothDevice>
    private lateinit var adapter: BleDeviceAdapter
    private lateinit var myBLEService: serviceBLE
    private var myBLEBound: Boolean = false


    private lateinit var notificationBellIcon: View
    private lateinit var backButton: Button
    private lateinit var btnStartScan: Button


    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_ble)

        PreferenceManager.init(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        backButton = findViewById(R.id.backButton)
        notificationBellIcon = findViewById(R.id.notifications)//todo figure out error and remove inflate suppression

        // Initialize RecyclerView and adapter
        val rvFoundDevices = findViewById<View>(R.id.rv_found_devices) as RecyclerView
        foundDevices = mutableListOf()
        adapter = BleDeviceAdapter(foundDevices, this)

        rvFoundDevices.adapter = adapter
        rvFoundDevices.layoutManager = LinearLayoutManager(this)


        //initialize bt manager
        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        // Initialize the start scan button
        btnStartScan = findViewById(R.id.btn_start_scan)
        btnStartScan.setOnClickListener {
                handleStartScanButtonClick(rvFoundDevices)
        }
//
        BackButtonService.setup(backButton, this)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun handleStartScanButtonClick(rvFoundDevices: RecyclerView) {
        when (PermissionsUtilities.checkPermissionsGranted(
            this,
            BleScanRequiredPermissions.permissions
        )) {
            true -> {
                if (myBLEBound) {
                    myBLEService.requestBleScan()
                    Log.d("alex log", " bluetoothBLEActivity BLE scan requested")

                    // Wait for the scan to complete before updating UI
                    waitForScanToComplete(rvFoundDevices)

//                    foundDevices.addAll(myBLEService.getFoundDevices()) // Update with new devices
                  //  Log.d("alex log", foundDevices.count().toString()+" "+foundDevices.toString())
                }
            }

            false -> PermissionsUtilities.checkPermissions(

                this, BleScanRequiredPermissions.permissions, BLE_PERMISSION_REQUEST_CODE
            )
        }


    }
    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this@BluetoothBLEActivity, serviceBLE::class.java)
        Log.d("alex log", " bluetoothBLEActivity Attempting to bind to serviceBLE")
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }
    private fun waitForScanToComplete(rvFoundDevices: RecyclerView) {
        Thread {
            // Wait for the scan to complete or a timeout (if necessary)
            // For simplicity, let's assume it takes some time and then returns the scanned devices

            // Simulate a delay for demonstration purposes
            Thread.sleep(1000)

            // Retrieve the scanned devices
            val scannedDevices = myBLEService.getFoundDevices()

            // Update the UI with the scanned devices on the main thread
            runOnUiThread {
               // Log.d("alex log", " bluetoothBLEActivity Updating UI with scanned devices: $scannedDevices")

                // Update foundDevices and notify adapter
                for(device in foundDevices){
                    if (!foundDevices.contains(device)) {
                        foundDevices.add(device)
//                            Log.d("ServieBLE_device", getFoundDevices().toString())
                    }
                }

                adapter = BleDeviceAdapter(foundDevices, this)
                adapter.notifyItemInserted(foundDevices.count()-1) // Notify adapter of data changes

             //   Log.d("alex log", foundDevices.toString())
                rvFoundDevices.adapter = adapter
                rvFoundDevices.layoutManager = LinearLayoutManager(this)
            }
        }.start()
    }


    private val connection = object : ServiceConnection {
        //is called on service bind, idk how android magic I think
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as serviceBLE.LocalBinder
            myBLEService = binder.getService()
            myBLEBound = true
            Log.d("alex log", " bluetoothBLEActivity connection ");

            foundDevices = myBLEService.getFoundDevices()
//            adapter = BleDeviceAdapter(foundDevices, this)

        }

        override fun onServiceDisconnected(className: ComponentName) {
            myBLEBound = false
        }

    }


    override fun onStop() {
        unbindService(connection)
        Log.d("alex log", " bluetoothBLEActivity unBind LOG!");
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
            onGrantedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { myBLEService.requestBleScan() }),
            onDeniedMap = mapOf(BLE_PERMISSION_REQUEST_CODE to { Toast.makeText(this,
                "Some permissions were not granted, please grant in system settings, and try again",
                Toast.LENGTH_LONG).show() })
        )
    }

    companion object {
        private const val BLE_PERMISSION_REQUEST_CODE = 1
    }

    override fun onItemClick(position: Int, device: BluetoothDevice) {
     //   Log.d("BLEActivity", "Clicked Item "+device.address)
        myBLEService.setSelectedDevice(device)
        myBLEService.connectToDevice(this)
        var myDevice = myBLEService.getSelectedCharacteristic()
        if (myDevice != null) {
            Log.d("alex Log", "Clicked Item "+myDevice.uuid.toString())
        }
    }

    //infalte the menues in the tool bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    //Logic for the different menu options (what activity to inflate).
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications
            R.id.notifications -> {
                // Sample notifications (replace with your actual notifications)
                val notifications = PreferenceManager.getNotifications()
                NotificationManager.showNotificationList(this, notificationBellIcon, notifications)
                true
            }
            R.id.configurationSettings -> {
                val operationSettings = Intent(this, ConfigurationSettingsActivity::class.java)
                startActivity(operationSettings)
                true
            }
            R.id.accessibilitySettings -> {
                val accessibilitySettings = Intent(this, AccessibilitySettingsActivity::class.java)
                startActivity(accessibilitySettings)
                true
            }
            R.id.bluetoothBLE -> {
                val bluetoothBLEActivity = Intent(this, BluetoothBLEActivity::class.java)
                startActivity(bluetoothBLEActivity)
                true
            }


            else -> false
        }
    }
}
