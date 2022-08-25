package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.AbsDeviceInteraction
import cc.fastcv.ble.sdk.ConnectStateChangeCallback
import cc.fastcv.ble.sdk.OTAUpgradeListener
import cc.fastcv.ble.sdk.log.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

class RingDeviceInteraction(private val callback: ConnectStateChangeCallback) :
    AbsDeviceInteraction("ring"),ConnectStateChangeCallback {

    /**
     * 戒指SDK支持
     */
    private val ringSdkSupport = RingSdkSupport(this)

    /**
     * 超时任务
     */
    private val timeoutTask = TimeoutTask()

    /**
     * 自动重连任务
     */
    private val autoTryConnectTask = AutoTryConnectTask()

    /**
     * 连接状态
     */
    private var connectedState = false

    /**
     * 获取当前连接状态
     */
    fun getConnectedState() = connectedState

    /**
     * 标记是否退出
     */
    private var quit = false

    /**
     * 标记是否需要自动
     */
    private var allowAutoConnect = true

    override fun disConnectDevice() {
        //主动断开设备,取消自动连接
        allowAutoConnect = false
        ringSdkSupport.disConnect()
    }

    override fun connectDevice(macAddress: String) {
        //主动连接设备,恢复自动连接
        allowAutoConnect = true
        ringSdkSupport.connect(macAddress)
        //启动连接超时任务 30秒
        sendMessageDelayed(timeoutTask, 30 * 1000L)
    }

    /**
     * 取消连接操作
     */
    override fun cancelConnect() {
        //取消超时任务
        removeMessage(timeoutTask)
        //主动断开设备,取消自动连接
        allowAutoConnect = false
        //取消连接
        ringSdkSupport.cancelConnect()
    }

    override fun closeAndQuitSafely() {
        quit = true
        ringSdkSupport.disConnect()
    }

    /**
     * 写入指令
     */
    fun writeCommand(cmd: String) {
        sendMessage {
            val result = ringSdkSupport.writeCommand(cmd)
            Logger.log("写入$cmd 指令：$result")
        }
    }

    /**
     * 设置消息接收值
     */
    fun setMsgReceiver(appProtocolImpl: IRingAppProtocol) {
        sendMessage {
            ringSdkSupport.setMsgReceiver(appProtocolImpl)
            Logger.log("设置消息接收者")
        }
    }

    /**
     * OTA升级
     */
    fun startOTAUpgrade(file: File, listener: OTAUpgradeListener) {
        sendMessage {
            Logger.log("开始OTA升级")
            runBlocking {
                ringSdkSupport.initOTATools(file, listener)
                delay(5000)
                ringSdkSupport.startUpgrade()
            }
        }
    }

    /**
     * 超时任务
     */
    inner class TimeoutTask : Runnable {
        override fun run() {
            //停止扫描
            ringSdkSupport.stopScan()
            //停止连接
            cancelConnect()
            //通知超时
            onConnectTimeout(ringSdkSupport.getTargetDeviceMacAddress())
        }
    }

    /**
     * 自动重连任务
     */
    inner class AutoTryConnectTask : Runnable {
        override fun run() {
            //停止扫描
            ringSdkSupport.stopScan()
            //开始扫描
            ringSdkSupport.startScan()
        }
    }

    /*********  连接状态变更回调  *********/

    /**
     * 连接中
     */
    override fun onConnecting(macAddress: String) {
        callback.onConnecting(macAddress)
    }

    /**
     * 已连接
     */
    override fun onConnected(macAddress: String) {
        connectedState = true
        removeMessage(timeoutTask)
        callback.onConnected(macAddress)

    }

    /**
     * 断开连接中
     */
    override fun onDisconnecting(macAddress: String) {
        callback.onDisconnecting(macAddress)
    }

    /**
     * 已断开连接
     */
    override fun onDisconnected(macAddress: String) {
        connectedState = false
        removeMessage(timeoutTask)
        callback.onDisconnected(macAddress)
        if (quit) {
            quitSafely()
        } else if (allowAutoConnect) {
            Logger.log("开始自动连接")
            sendMessage(autoTryConnectTask)
        }
    }

    /**
     * 连接超时
     */
    override fun onConnectTimeout(macAddress: String) {
        callback.onConnectTimeout(macAddress)
    }
}