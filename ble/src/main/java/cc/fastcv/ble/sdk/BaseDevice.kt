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
    abstract var deviceInfo : IDeviceInfo

    /**
     * 设备交互器
     */
    abstract var interaction : AbsDeviceInteraction

    /***** 提供的设备通用基础接口 *****/
    fun connectDevice() {
        interaction.connectDevice(deviceInfo.getEffectiveMacAddress())
    }

    fun disConnectDevice() {
        interaction.disConnectDevice()
    }

    /**
     * 设备被添加时
     */
    abstract fun callByAddToList()

    /**
     * 设备被移除时
     */
    abstract fun callByRemoveToList()

    /**
     * 切换设备时
     */
    abstract fun callByCancelUsing(supportMultiDevice: Boolean)

    /**
     * 切换设备时
     */
    abstract fun callByStartUsing(supportMultiDevice: Boolean)
}