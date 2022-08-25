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

    /**
     * 设备列表
     */
    private var deviceList = mutableListOf<BaseDevice>()

    /**
     * 当前设备
     */
    private var curDevice: BaseDevice?= null

    /**
     * 是否支持多设备
     * 默认不支持
     */
    private var supportMultiDevice = false

    /***** 提供的接口 *****/

    /**
     * 获取设备列表
     */
    fun getDeviceList() = deviceList

    /**
     * 获取当前设备
     */
    fun getCurDevice() = curDevice

    fun setSupportMultiDevice(supportMultiDevice: Boolean) {
        this.supportMultiDevice = supportMultiDevice
    }

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
        notifyDeviceChange(oldDevice,newDevice)
        oldDevice?.callByCancelUsing(supportMultiDevice)
        newDevice?.callByStartUsing(supportMultiDevice)
    }

    /**
     * 初始化设备列表
     */
    fun initDeviceList() {
        //如果传入的列表数据为空,则访问本地数据（为了支持单机版可以正常使用）
//        if () {
//
//        } else {
//
//        }

        //拿到列表后,循环解析出对应的 Device对象

        //将获取到的 Device对象存入 设备列表中

        //获取上次连接的设备信息（如果没有则选中第一个）

        //切换到选中的设备
    }
}