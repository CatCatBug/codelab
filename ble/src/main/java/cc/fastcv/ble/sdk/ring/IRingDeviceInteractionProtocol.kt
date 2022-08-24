package cc.fastcv.ble.sdk.ring

interface IRingDeviceInteractionProtocol {
    /**
     * 开始连接/连接中
     */
    fun onConnecting(macAddress: String)

    /**
     * 连接成功
     */
    fun onConnected(macAddress: String)

    /**
     * 断开连接/断开中
     */
    fun onDisconnecting(macAddress: String)

    /**
     * 断开连接
     */
    fun onDisconnected(macAddress: String)

    /**
     * 特征值变更回调
     */
    fun onCharacteristicChanged(value : ByteArray?)

    /**
     * 最大传输单元变更回调
     */
    fun onMtuChanged(status: Int, mtu: Int)

    /**
     * 特征值写入状态回调
     */
    fun onCharacteristicWrite(status: Int)

}