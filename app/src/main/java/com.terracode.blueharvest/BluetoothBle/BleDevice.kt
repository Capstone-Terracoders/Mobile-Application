package com.terracode.blueharvest.BluetoothBle

data class BleDevice(val name: String) {
    companion object {
        fun createBleDevicesList(): MutableList<BleDevice> {
            return mutableListOf()
        }
    }
}
