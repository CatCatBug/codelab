package cc.fastcv.ble.sdk

interface DeviceChangeCallback {
    fun onDeviceChanged(oldDevice:BaseDevice?, newDevice:BaseDevice?)
}