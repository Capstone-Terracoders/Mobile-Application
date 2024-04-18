package com.terracode.blueharvest.BluetoothBle

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

/* extends
A new BLE device is discovered.
A batch of scan results is ready.
The BLE scan fails for some reason.
The Core Class: The central piece is the android.bluetooth.le.ScanCallback */

/**
 * This class provides callback methods for handling Bluetooth LE scan results
 * and errors.
 */

class BleScanCallback(

    /**
     * Callback function invoked when a single BLE device is discovered during the scan.
     *
     * @param result The ScanResult object containing details about the discovered device,
     * or null if there was an error.
     */
    private val onScanResultAction: (ScanResult?) -> Unit = {},
    /**
     * Callback function invoked when a batch of BLE scan results is available.
     *
     * @param results A list of ScanResult objects containing details about the
     * discovered devices, or null if there were no results or an error occurred.
     */

    private val onBatchScanResultAction: (MutableList<ScanResult>?) -> Unit = {},
    /**
     * Callback function invoked when the BLE scan fails.
     *
     * @param errorCode The error code indicating the reason for the scan failure.
     */
    private val onScanFailedAction: (Int) -> Unit = {}
) : ScanCallback() {
    /**
     * Invoked when a single BLE device is discovered during the scan.
     * This method delegates the handling of the ScanResult to the provided
     * onScanResultAction callback function.
     *
     * @param callbackType The type of scan callback, does not disinguish between type, commented out.
     * @param result The ScanResult object containing details about the discovered device,
     * or null if there was an error.
     */
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        /*
        when (callbackType) {
            ScanCallback.SCAN_RESULT -> {
                // Handle a single discovered device
                onScanResultAction(result)
            }
            ScanCallback.SCAN_FAILED -> {
                // Handle scan failure
                val errorCode = result?.errorCode ?: -1  // Get error code or default to -1
                onScanFailedAction(errorCode)
            }
            ScanCallback.BATCH_SCAN_RESULTS -> {
                // Handle a batch of scan results (API level 21+)
                val results = result?.toList() ?: emptyList()  // Handle null result
                onBatchScanResultsAction(results)
            }
            ScanCallback.SCAN_PERIOD_MODIFIED -> {
                // Handle scan period modification (API level 26+)
                // (Optional handling if relevant)
            }
            else -> {
                // Handle unexpected callback types (for future-proofing)
                // Log a warning or take appropriate action
            }
        */

        onScanResultAction(result)
    }
    /**
     * Invoked when a single BLE device is discovered during the scan.
     * This method delegates the handling of the ScanResult to the provided
     * onScanResultAction callback function.
     *
     * @param callbackType The type of scan callback (ignored in this implementation).
     * @param result The ScanResult object containing details about the discovered device,
     * or null if there was an error.
     */
    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        super.onBatchScanResults(results)
        onBatchScanResultAction(results)
    }
    /**
     * Invoked when the BLE scan fails.
     * This method delegates the handling of the error code to the provided
     * onScanFailedAction callback function.
     *
     * @param errorCode The error code indicating the reason for the scan failure.
     */
    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        onScanFailedAction(errorCode)
    }
}
