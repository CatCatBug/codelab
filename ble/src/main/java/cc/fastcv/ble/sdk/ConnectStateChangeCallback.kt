package cc.fastcv.ble.sdk

/**
 * 连接状态回调接口
 */
interface ConnectStateChangeCallback {

    /**
     * 开始连接/连接中
     * 默认空实现
     */
    fun onConnecting(macAddress: String) {}

    /**
     * 连接成功
     */
    fun onConnected(macAddress: String)

    /**
     * 断开连接/断开中
     * 默认空实现
     */
    fun onDisconnecting(macAddress: String) {}

    /**
     * 断开连接
     */
    fun onDisconnected(macAddress: String)

    /**
     * 连接超时
     */
    fun onConnectTimeout(macAddress: String)

}