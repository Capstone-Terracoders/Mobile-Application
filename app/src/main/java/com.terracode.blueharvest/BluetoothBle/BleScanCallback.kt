package com.terracode.blueharvest.BluetoothBle

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

/* extends
A new BLE device is discovered.
A batch of scan results is ready.
The BLE scan fails for some reason.
The Core Class: The central piece is the android.bluetooth.le.ScanCallback */



class BleScanCallback(

    private val onScanResultAction: (ScanResult?) -> Unit = {},
    private val onBatchScanResultAction: (MutableList<ScanResult>?) -> Unit = {},
    private val onScanFailedAction: (Int) -> Unit = {}
) : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)

        onScanResultAction(result)
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        super.onBatchScanResults(results)
        onBatchScanResultAction(results)
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        onScanFailedAction(errorCode)
    }
}
