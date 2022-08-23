package cc.fastcv.ble.sdk

class EmptyDeviceInteraction : AbsDeviceInteraction("empty") {
    override fun disConnectDevice() {

    }

    override fun connectDevice(macAddress: String) {
    }

    override fun cancelConnect() {
    }
}