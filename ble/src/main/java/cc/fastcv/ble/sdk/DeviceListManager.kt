package cc.fastcv.ble.sdk

object DeviceListManager {
    /***** 提供的监听接口 *****/

    /**
     * 设备列表的切换回调
     */
    private val deviceChangeCallbacks = mutableListOf<DeviceChangeCallback>()

    fun registerDeviceChangeCallback(callback: DeviceChangeCallback) {
        deviceChangeCallbacks.add(callback)
    }

    fun unRegisterDeviceChangeCallback(callback: DeviceChangeCallback) {
        deviceChangeCallbacks.remove(callback)
    }

    private fun notifyDeviceChange(oldDevice:BaseDevice?, newDevice:BaseDevice?) {
        deviceChangeCallbacks.forEach {
            it.onDeviceChanged(oldDevice,newDevice)
        }
    }

    //设备列表
    private var deviceList = mutableListOf<BaseDevice>()

    //当前设备
    private var curDevice: BaseDevice?= null

   /***** 提供的接口 *****/

    /**
     * 新增一个设备
     */
    fun addNewDevice(newDevice: BaseDevice) {
        if (!deviceList.contains(newDevice)) {
            deviceList.add(newDevice)
            newDevice.callByAddToList()
        }
    }

    /**
     * 删除设备
     */
    fun removeDevice(device: BaseDevice) {
        if (deviceList.contains(device)) {
            deviceList.remove(device)
            device.callByRemoveToList()
        }
    }

    /**
     * 切换设备
     */
    fun switchDevice(oldDevice: BaseDevice?,newDevice: BaseDevice?) {
        curDevice = newDevice
        oldDevice?.callByCancelUsing()
        newDevice?.callByStartUsing()
        notifyDeviceChange(oldDevice,newDevice)
    }

    /**
     * 获取设备列表
     */
    fun getDeviceList() = deviceList

    /**
     * 获取当前设备
     */
    fun getCurDevice() = curDevice
}