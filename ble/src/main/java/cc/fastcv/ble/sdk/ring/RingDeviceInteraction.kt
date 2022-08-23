package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.AbsDeviceInteraction
import cc.fastcv.ble.sdk.ConnectStateChangeCallback
import cc.fastcv.ble.sdk.OTAUpgradeListener
import cc.fastcv.ble.sdk.log.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

class RingDeviceInteraction(private val callback: ConnectStateChangeCallback) :
    AbsDeviceInteraction("ring"), ConnectStateChangeCallback, IRingOTAProtocol {

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

    /**
     * OTA升级的工具类
     */
    private val ringOTAUpgradeTool = RingOTAUpgradeTool()

    override fun disConnectDevice() {
        connectedManager.disConnect()
    }

    override fun connectDevice(macAddress: String) {
        connectedManager.connect(macAddress)
        //启动连接超时任务 30秒
        sendMessageDelayed(timeoutTask, 30 * 1000L)
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
        callback.onConnecting(macAddress)
    }

    override fun onConnected(macAddress: String) {
        removeMessage(timeoutTask)
        callback.onConnected(macAddress)

    }

    override fun onDisconnecting(macAddress: String) {
        callback.onDisconnecting(macAddress)
    }

    override fun onDisconnected(macAddress: String) {
        removeMessage(timeoutTask)
        callback.onDisconnected(macAddress)
    }

    override fun onConnectTimeout(macAddress: String) {
        callback.onConnectTimeout(macAddress)
    }

    /**
     * 写入指令
     */
    fun writeCommand(cmd: String) {
        sendMessage {
            val result = connectedManager.writeCommand(cmd)
            Logger.log("写入$cmd 指令：$result")
        }
    }

    /**
     * OTA升级
     */
    fun startOTAUpgrade(file: File, listener: OTAUpgradeListener) {
        sendMessage {
            Logger.log("开始OTA升级")
            runBlocking {
                ringOTAUpgradeTool.init(file, this@RingDeviceInteraction, listener)
                delay(5000)
                ringOTAUpgradeTool.startUpgrade()
            }
        }
    }

    override fun writeIntCommand(
        type: Int,
        address: Int,
        buffer: ByteArray?,
        length: Int
    ): Boolean {
        return connectedManager.writeIntCommand(type,address,buffer,length)
    }

    override fun writeLongCommand(
        type: Int,
        address: Int,
        buffer: ByteArray?,
        length: Long
    ): Boolean {
        return connectedManager.writeLongCommand(type,address,buffer,length)
    }

    override fun requestMtu(mtu: Int) {
        connectedManager.requestMtu(mtu)
    }

    override fun setBleOTANotify() {
        connectedManager.setBleOTANotify()
    }
}