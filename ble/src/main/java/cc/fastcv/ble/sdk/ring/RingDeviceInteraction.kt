package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.AbsDeviceInteraction
import cc.fastcv.ble.sdk.ConnectStateChangeCallback

class RingDeviceInteraction(callback: ConnectStateChangeCallback) : AbsDeviceInteraction("ring"), ConnectStateChangeCallback {

    /**
     * 设备连接器
     */
    private val connectedManager = RingDeviceConnectedManager(this)

    /**
     * 设备扫描器
     */
    private val scanner = RingScanner()

    /**
     * 超时任务
     */
    private val timeoutTask = TimeoutTask()

    override fun disConnectDevice() {
        connectedManager.disConnect()
    }

    override fun connectDevice(macAddress: String) {
        connectedManager.connect(macAddress)
        //启动连接超时任务 30秒
        sendMessageDelayed(timeoutTask,30*1000L)
    }

    /**
     * 取消连接操作
     */
    override fun cancelConnect() {
        //取消连接操作
        //取消超时任务
        removeMessage(timeoutTask)
        //停止扫描
        scanner.stopScan()
        //调用连接器取消连接
        connectedManager.cancelConnect()
    }


    inner class TimeoutTask : Runnable {
        override fun run() {
            //停止扫描
            scanner.stopScan()
            //停止连接
            cancelConnect()
            //通知超时
            onConnectTimeout(connectedManager.getTargetDeviceMacAddress())
        }
    }

    override fun onConnecting(macAddress: String) {

    }

    override fun onConnected(macAddress: String) {
        removeMessage(timeoutTask)

    }

    override fun onDisconnecting(macAddress: String) {

    }

    override fun onDisconnected(macAddress: String) {
        removeMessage(timeoutTask)
    }

    override fun onConnectTimeout(macAddress: String) {
    }


}