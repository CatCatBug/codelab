package cc.fastcv.ble.sdk

/**
 * 基础设备的通用接口
 */
abstract class BaseDevice {
    /**
     * 设备信息
     */
    private var deviceInfo : BaseDeviceInfo = BaseDeviceInfo()

    /**
     * 设备交互器
     */
    private lateinit var interaction : AbsDeviceInteraction

    /***** 提供的接口 *****/

    fun getInfo() : BaseDeviceInfo = deviceInfo

    fun connectDevice() {
        if (deviceInfo.isEffectiveMacAddress()) {
            interaction.connectDevice(deviceInfo.macAddress)
        }
    }

    fun disConnectDevice() {
        interaction.disConnectDevice()
    }

    fun callByAddToList() {
        //设备被添加时,需要启动设备交互器
        interaction.start()
    }

    fun callByRemoveToList() {
        //设备被移除时,需要停止设备交互器
        interaction.quitSafely()
    }

    fun callByCancelUsing() {
        //切换设备时,如果取消使用后，则断开设备连接
        interaction.disConnectDevice()
    }

    fun callByStartUsing() {
        //切换设备时,如果开始使用后，则开始连接设备
        interaction.connectDevice(deviceInfo.macAddress)
    }
}