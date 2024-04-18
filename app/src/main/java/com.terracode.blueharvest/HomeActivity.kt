package com.terracode.blueharvest

import android.bluetooth.BluetoothGattCharacteristic
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.terracode.blueharvest.BluetoothBle.BluetoothBLEActivity
import com.terracode.blueharvest.BluetoothBle.serviceBLE
import com.terracode.blueharvest.services.toolbarServices.RecordButtonService
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.viewManagers.LocaleManager
import com.terracode.blueharvest.utils.viewManagers.NotificationManager
import com.terracode.blueharvest.utils.viewManagers.TextSizeManager
import com.terracode.blueharvest.utils.viewManagers.ThemeManager
import java.util.UUID


/**
 * Activity class for the Accessibility Settings Page
 *
 * @authors MacKenzie Young, Alexandra Gale, Dharani Singaram, Anthony Caccese
 * Last Updated: 3/2/2024
 *
 */

val Sensor1uuid = UUID.fromString("5a4ed7f3-221d-47c3-991b-09cca7ea00dc")

class HomeActivity : AppCompatActivity() {

    //Declaring service to start
    private lateinit var myBLEService: serviceBLE
    private var myBLEBound: Boolean = false

    //Declaring the TextViews for the data values as TextView type.
    private lateinit var optimalRakeHeightTextView: TextView
    private lateinit var optimalRakeRPMValueTextView: TextView
    private lateinit var currentBushHeightTextView: TextView
    private lateinit var currentSpeedTextView: TextView
    private lateinit var recordButton: Button
    private lateinit var notificationBellIcon: View
    private lateinit var serviceIntent: Intent

    //Declare value types
    private var cm = "cm"
    private var inch = "in"
    private var kmph = "km/h"
    private var mph = "mph"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize the sharedPreferences
        PreferenceManager.init(this)

     //   PreferenceManager.setMyBleService(false)

        //start the activity
        serviceIntent = Intent(this@HomeActivity, serviceBLE::class.java)
        //initialize and bind to service

        if(!PreferenceManager.getMyBleStarted()) {
            startService(Intent(this@HomeActivity, serviceBLE::class.java))
            Log.d("alex log", "start service call from home Activity")
            PreferenceManager.setMyBleStarted(true)

        }

        //Set setting values before setting the content view
        val currentTheme = ThemeManager.getCurrentTheme(this)
        ThemeManager.setColorOverlayTheme(this, currentTheme)

        //Set language
        val currentLanguagePosition = PreferenceManager.getSelectedLanguagePosition()
        val languagePosition = LocaleManager.getLanguageCode(currentLanguagePosition)
        LocaleManager.setLocale(this, languagePosition)

        //Set the view
        setContentView(R.layout.activity_home)

        //Set the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Set the text size
        val rootView = findViewById<View>(android.R.id.content).rootView
        TextSizeManager.setTextSizeView(this, rootView)

        //Set the declared TextView values equal to the IDs in the HomeActivity XML file.
        optimalRakeHeightTextView = findViewById(R.id.optimalRakeHeightValue)
        optimalRakeRPMValueTextView = findViewById(R.id.optimalRakeRPMValue)
        currentBushHeightTextView = findViewById(R.id.currentBushHeightValue)
        currentSpeedTextView = findViewById(R.id.currentSpeedValue)
        recordButton = findViewById(R.id.recordButton)
        notificationBellIcon = findViewById(R.id.notifications)

        // Calls Record Data Service
        RecordButtonService.setup(recordButton, this)

        //Get the value of the toggle from AccessibilitySettings XML file.
        val toggleValue = PreferenceManager.getSelectedUnit()

        //Read data from mock values/bluetooth, which we locate in the preference manager
        val bushHeightData = PreferenceManager.getBushHeight()
        val speedData = PreferenceManager.getSpeed()
        val optimalRakeHeight = PreferenceManager.getOptimalRakeHeight()
        val optimalRakeRpm = PreferenceManager.getOptimalRakeRpm()


        //Set the value of the text on the XML file equal to the data values depending on if the toggle is switched.
        optimalRakeRPMValueTextView.text = optimalRakeRpm.toString()

        if (toggleValue) {
            val optimalRakeHeightText = "$optimalRakeHeight $cm"
            optimalRakeHeightTextView.text = optimalRakeHeightText
            val currentBushHeightText = "$bushHeightData $cm"
            currentBushHeightTextView.text = currentBushHeightText
            val currentSpeedText = "$speedData $kmph"
            currentSpeedTextView.text = currentSpeedText

        } else {
            val optimalRakeHeightText = "$optimalRakeHeight $inch"
            optimalRakeHeightTextView.text = optimalRakeHeightText
            val currentBushHeightText = "$bushHeightData $inch"
            currentBushHeightTextView.text = currentBushHeightText
            val currentSpeedText = "$speedData $mph"
            currentSpeedTextView.text = currentSpeedText

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("alex log", " Home Activity Attempting to bind to serviceBLE")
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }
    override fun onStop() {
        unbindService(connection)
        Log.d("alex log", " Home Activity unBind LOG!")
        super.onStop()
    }

    override fun onDestroy() {
        unbindService(connection)
        myBLEService.stopService(serviceIntent)
        super.onDestroy()
        Log.d("alex log", " Home Activity unBind LOG!")


        //todo stop the service
    }
    //connection object for bluetooth service
    private val connection = object : ServiceConnection {
        //is called on service bind, idk how android magic I think
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d("alex log", " Home Activity connection object called ")
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as serviceBLE.LocalBinder
            myBLEService = binder.getService()
            val myGatt = myBLEService.getGatt()
            val services = myGatt?.services                          //See if the service discovery was successful
            Log.d("alex log ", "servicessssssss home activity: $services")
            // val services = gatt?.services


            if (services == null) { Log.d("alex log", "services null onServiceConnected home act") }
            if (services != null) {
                for (service in services) {
                    val characteristics = service.characteristics

                    for (characteristic in characteristics) {
                        if (characteristic.uuid.equals(Sensor1uuid) ) {
                            Log.d("alex log", " characteristic match onServiceConnected home act")


                            val readListener = object : serviceBLE.ReadListener {
                                override fun onValueObtained(characteristic: BluetoothGattCharacteristic, value: ByteArray) {
                                    // Handle the read value
                                    val stringValue = String(value, Charsets.UTF_8)
                                    Log.d("alex log", " $characteristic val: ${value.size}")
                                }
                            }
                            // Call readCharacteristic with the defined listener
                            myBLEService.readCharacteristic(characteristic, readListener)

                        }
                      //  Log.d("alex log", characteristic.uuid.toString()+ "    stuff ${service.uuid}"  )
                    }
                }
            }
        }
//            val characteristic = myBLEService.getSelectedCharacteristic()
//            if (characteristic != null) {
//                Log.d("alex Log", "kslejghfosiehgno")
//                myBLEService.readCharacteristic(characteristic)
//            }
//            myBLEBound = true
        override fun onServiceDisconnected(className: ComponentName) {
            myBLEBound = false
        }
        }


    //Inflates the menu in the toolbar.
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
                Log.d("HomeActivity-NotificationItem", "You selected notifications!")
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
