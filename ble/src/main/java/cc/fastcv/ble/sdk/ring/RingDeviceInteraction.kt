package cc.fastcv.ble.sdk.ring

import android.bluetooth.BluetoothGatt
import cc.fastcv.ble.sdk.AbsDeviceInteraction
import cc.fastcv.ble.sdk.ConnectStateChangeCallback
import cc.fastcv.ble.sdk.OTAUpgradeListener
import cc.fastcv.ble.sdk.log.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

class RingDeviceInteraction(private val callback: ConnectStateChangeCallback) :
    AbsDeviceInteraction("ring"), IRingDeviceInteractionProtocol, IRingOTAProtocol {

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

    /**
     * 指令解析器
     */
    private val ringCommandParser = RingCommandParser()

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

    override fun closeAndQuitSafely() {
        quit = true
        connectedManager.disConnect()
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
     * 设置消息接收值
     */
    fun setMsgReceiver(appProtocolImpl: IRingAppProtocol) {
        sendMessage {
            ringCommandParser.setMsgReceiver(appProtocolImpl)
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
                ringOTAUpgradeTool.init(file, this@RingDeviceInteraction, listener)
                delay(5000)
                ringOTAUpgradeTool.startUpgrade()
            }
        }
    }

    /**
     * 超时任务
     */
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
        }
    }

    /**
     * 连接超时
     */
    fun onConnectTimeout(macAddress: String) {
        callback.onConnectTimeout(macAddress)
    }

    override fun onCharacteristicChanged(value: ByteArray?) {
        Logger.log("特征值改变,返回结果不为空 ${value != null}")
        value?.let {
            ringOTAUpgradeTool.setReceiveValue(value)
            ringCommandParser.handlerReceiveResult(String(it))
        }
    }

    override fun onMtuChanged(status: Int, mtu: Int) {
        Logger.log("收到mtu信息回调 status：$status  mtu：$mtu")
        if (BluetoothGatt.GATT_SUCCESS == status) {
            ringOTAUpgradeTool.setMtuChangeSateAndValue(true, mtu)
        } else {
            ringOTAUpgradeTool.setMtuChangeSateAndValue(true, 235)
        }
    }

    override fun onCharacteristicWrite(status: Int) {
        Logger.log("收到写入指令的状态回调:$status")
        ringOTAUpgradeTool.setWriteStatus(status)
    }

    /*********  OTA升级需要提供的接口  *********/

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