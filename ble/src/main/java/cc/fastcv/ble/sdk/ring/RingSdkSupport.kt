package cc.fastcv.ble.sdk.ring

import android.bluetooth.BluetoothGatt
import cc.fastcv.ble.sdk.ConnectStateChangeCallback
import cc.fastcv.ble.sdk.OTAUpgradeListener
import cc.fastcv.ble.sdk.log.Logger
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.io.File

class RingSdkSupport(private val callback: ConnectStateChangeCallback) : IRingDeviceInteractionProtocol,IRingOTAProtocol {

    /**
     * 目标设备mac地址
     */
    private var targetDeviceMacAddress = ""

    /**
     * 设备连接器
     */
    private val connectedManager = RingDeviceConnectedManager(this)

    /**
     * 扫描结果的回调
     */
    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)
            results.forEach { result ->
                if (result.device.address == getTargetDeviceMacAddress()) {
                    stopScan()
                    connectedManager.connect(targetDeviceMacAddress)
                }
            }
        }
    }

    /**
     * 设备扫描器
     */
    private val scanner = RingScanner(scanCallback)

    /**
     * OTA升级的工具类
     */
    private val ringOTAUpgradeTool = RingOTAUpgradeTool()

    /**
     * 指令解析器
     */
    private val ringCommandParser = RingCommandParser()

    fun disConnect() {
        connectedManager.disConnect()
    }

    fun connect(macAddress: String) {
        targetDeviceMacAddress = macAddress
        startScan()
    }

    fun cancelConnect() {
        scanner.stopScan()
        connectedManager.cancelConnect()
    }

    fun writeCommand(cmd: String) = connectedManager.writeCommand(cmd)
    fun setMsgReceiver(appProtocolImpl: IRingAppProtocol) {
        ringCommandParser.setMsgReceiver(appProtocolImpl)
    }

    fun initOTATools(
        file: File,
        listener: OTAUpgradeListener
    ) {
        ringOTAUpgradeTool.init(file,this,listener)
    }

    fun startUpgrade() {
        ringOTAUpgradeTool.startUpgrade()
    }

    fun stopScan() {
        scanner.stopScan()
    }

    fun getTargetDeviceMacAddress() = targetDeviceMacAddress

    fun startScan() {
        scanner.stopScan()
        scanner.startScan()
    }

    override fun onConnecting(macAddress: String) {
        callback.onConnecting(macAddress)
    }

    override fun onConnected(macAddress: String) {
        callback.onConnected(macAddress)
    }

    override fun onDisconnecting(macAddress: String) {
        callback.onDisconnecting(macAddress)
    }

    override fun onDisconnected(macAddress: String) {
        callback.onDisconnected(macAddress)
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