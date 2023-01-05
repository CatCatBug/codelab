package cc.fastcv.bluetoothdemo.bt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

class BleDeviceProxy(private val device: BluetoothDevice, val rssi: Int) {
    fun getAddress(): String {
        return device.address
    }

    @SuppressLint("MissingPermission")
    fun getName():String? {
        return device.name
    }
}