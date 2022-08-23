package cc.fastcv.ble.sdk

/**
 * 抽象的设备
 * 一个通用的设备肯定包含两个部分
 * 1、设备信息
 * 2、设备交互（即设备端提供的接口和回调）
 *
 * 关于接口的话，不同的人有不同的实现
 * 所以，这里定义的就是在BaseDevice的子类中提供具体接口方法
 * 然后再用各自的实现方式将命令转成写给设备的指令
 * 最后通过设备交互提供的指令写入方法去写给设备
 *
 * 关于回调，BLE协议的底层回调是一致的
 * 所以，回调到Device的是原始数据
 * 通过各自的解析器将原始数据解析出来分发即可
 *
 * 关于设备可能的网络数据的处理
 */
abstract class BaseDevice {
    /**
     * 设备信息
     */
    private var deviceInfo : BaseDeviceInfo = BaseDeviceInfo()

    /**
     * 设备交互器
     */
    protected open var interaction : AbsDeviceInteraction = EmptyDeviceInteraction()

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