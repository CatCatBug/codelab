package cc.fastcv.ble.sdk

import android.bluetooth.BluetoothAdapter

class BaseDeviceInfo {

    /**
     * 设备类型 自行定义
     */
    var deviceType : Int = 0

    /**
     * 设备名称
     */
    var deviceName : String = ""

    /**
     * 设置mac地址
     */
    var macAddress : String = ""

    fun isEffectiveMacAddress() : Boolean {
        return BluetoothAdapter.checkBluetoothAddress(macAddress)
    }

}