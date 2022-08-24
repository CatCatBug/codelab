package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.AbsDeviceInteraction
import cc.fastcv.ble.sdk.BaseDevice
import cc.fastcv.ble.sdk.IDeviceInfo

class RingDevice : BaseDevice() {
    /**
     * 设备信息
     */
    override var deviceInfo: IDeviceInfo = RingDeviceInfo()

    /**
     * 设备连接状态管理者及分发器
     */
    private val ringDeviceConnectStateManager = RingDeviceConnectStateManager()

    /**
     * 设备交互器
     */
    override var interaction : AbsDeviceInteraction = RingDeviceInteraction(ringDeviceConnectStateManager)

    /**
     * 设备接口提供者
     */
    private val defaultRingDeviceProtocolImpl = DefaultRingDeviceProtocolImpl(interaction as RingDeviceInteraction)

    /**
     * 设备回调处理分发器
     */
    private val defaultRingAppProtocolImpl = DefaultRingAppProtocolImpl(interaction as RingDeviceInteraction)

    /**
     * 提供给外部调用接口的入口
     */
    fun getDeviceInterface() = defaultRingDeviceProtocolImpl

    /**
     * 提供给外部监听回调的入口
     */
    fun getDeviceCallbackProvider() = defaultRingAppProtocolImpl

    /**
     * 提供给外部监听连接状态回调的入口
     */
    fun getConnectStateCallbackProvider() = ringDeviceConnectStateManager

    /**
     * 获取当前设备的连接状态
     */
    fun getConnectState() = (interaction as RingDeviceInteraction).getConnectedState()

}