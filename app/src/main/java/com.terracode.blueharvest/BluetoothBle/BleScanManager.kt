package com.terracode.blueharvest.BluetoothBle

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * This class manages Bluetooth LE scans for discovering nearby devices.
 * It provides methods to start and stop scans, configure scan duration, and
 * define actions to be executed before and after scans.
 */
class BleScanManager(
    btManager: BluetoothManager,
    /**
     * The duration of the BLE scan in milliseconds. Defaults to [DEFAULT_SCAN_PERIOD].
     */
    private val scanPeriod: Long = DEFAULT_SCAN_PERIOD,
    /**
     * The callback object used to handle BLE scan results and errors.
     */
    private val scanCallback: BleScanCallback = BleScanCallback()
)


{
    private val btAdapter = btManager.adapter
    private val bleScanner = btAdapter.bluetoothLeScanner

    var beforeScanActions: MutableList<() -> Unit> = mutableListOf()
    var afterScanActions: MutableList<() -> Unit> = mutableListOf()
    /**
     * A list of functions to be executed before and after, commented out in bleactivity, starting a BLE scan.
     */
    /** True when the manager is performing the scan */
    private var scanning = false

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Stops the ongoing BLE scan.
     */
    @SuppressLint("MissingPermission")
    fun stopScan() {
        scanning = false
        bleScanner.stopScan(scanCallback)

        // execute all the functions to execute after scanning
        executeAfterScanActions()
    }
    /**
     * Scans for Bluetooth LE devices and stops the scan after [scanPeriod] seconds.
     * Does not checks the required permissions are granted, check must be done beforehand.
     */
    @SuppressLint("MissingPermission")//best to remove these for security,, implement later
    fun scanBleDevices() {

        Log.d("serviceBLE", "scan BLEdevices, from manager LOG!")
        // scans for bluetooth LE devices
        if (scanning) {
            stopScan()
        } else {
            // stops scanning after scanPeriod millis
            handler.postDelayed({ stopScan() }, scanPeriod)
            // execute all the functions to execute before scanning
            executeBeforeScanActions()

            // starts scanning
            scanning = true
            bleScanner.startScan(scanCallback)
        }
    }
    private fun executeBeforeScanActions() {
        executeListOfFunctions(beforeScanActions)
    }

    private fun executeAfterScanActions() {
        executeListOfFunctions(afterScanActions)
    }


    companion object {
        const val DEFAULT_SCAN_PERIOD: Long = 10000

        private fun executeListOfFunctions(toExecute: List<() -> Unit>) {
            toExecute.forEach {
                it()
            }
        }
    }

}
