package cc.fastcv.ble.sdk.ring

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import cc.fastcv.ble.sdk.SDKManager
import cc.fastcv.ble.sdk.log.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * 戒指设备最底层连接交互的类
 */
@SuppressLint("MissingPermission","NewApi")
class RingDeviceConnectedManager(private val ringDeviceInteractionProtocol: IRingDeviceInteractionProtocol) :
    BluetoothGattCallback() {

    companion object {
        //读及notify特征值
        private const val UUID_NOTIFY_DATA = "0000d002-0000-1000-8000-00805f9b34fb"

        //绑定读及notify特征值使用的UUID
        private const val UUID_DES = "00002902-0000-1000-8000-00805f9b34fb"

        //写特征值：时间同步，电量同步，翻转显示，寻找设备
        const val UUID_RING_DATA = "0000d004-0000-1000-8000-00805f9b34fb"

        //OTA升级
        const val UUID_SEND_DATA = "0000ff01-0000-1000-8000-00805f9b34fb"
        const val UUID_SEND_DATA_H = "02f00000-0000-0000-0000-00000000ff01"

        const val UUID_RECV_DATA = "0000ff02-0000-1000-8000-00805f9b34fb"
        const val UUID_RECV_DATA_H = "02f00000-0000-0000-0000-00000000ff02"

        private const val OTA_CMD_NULL = 10


        private const val TARGET_INIT = 0
        private const val TARGET_CONNECTED = 1
        private const val TARGET_DISCONNECTED = 2
    }

    private var mGatt: BluetoothGatt? = null
    private var device: BluetoothDevice? = null

    /**
     * 连接状态
     */
    private var connectState: Boolean = false

    /**
     * 连接到设备
     */
    fun connect(address: String) {
        if (!connectState) return

        target = TARGET_CONNECTED
        val bluetoothManager = SDKManager.getApp()
            .getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        device =
            bluetoothManager.adapter?.getRemoteDevice(address)

        mGatt =
            device?.connectGatt(
                SDKManager.getApp(), false, this,
                BluetoothDevice.TRANSPORT_LE
            )
    }

    /**
     * 断开设备
     */
    fun disConnect() {
        if (connectState) {
            target = TARGET_DISCONNECTED
            mGatt?.disconnect()
        }
    }

    /*****  具体的原生支持方法   *****/
    private var ringNotifyDescriptor: BluetoothGattDescriptor? = null
    private var ringWriteCharacteristic: BluetoothGattCharacteristic? = null

    private var otaNotifyDescriptor: BluetoothGattDescriptor? = null
    private var otaNotifyCharacteristic: BluetoothGattCharacteristic? = null
    private var otaUpgradeCharacteristic: BluetoothGattCharacteristic? = null

    /**
     * 指令写入器
     */
    private var writerOperation: WriterOperation = WriterOperation()

    /**
     * 操作目标
     */
    private var target: Int = TARGET_INIT

    fun getTargetDeviceMacAddress() = device?.address?:""

    /**
     * 取消连接
     * 断开并清除服务
     */
    fun cancelConnect() {
        mGatt?.disconnect()
        mGatt?.close()
    }

    /**
     * 写入指令
     */
    fun writeCommand(cmd: String) : Boolean {
        return if (mGatt != null && ringWriteCharacteristic != null) {
            writerOperation.send_data(
                OTA_CMD_NULL,
                0,
                cmd.toByteArray(),
                cmd.toByteArray().size,
                ringWriteCharacteristic!!,
                mGatt
            )
        } else {
            false
        }
    }

    fun writeIntCommand(type: Int, address: Int, buffer: ByteArray?, length: Int): Boolean {
        return if (mGatt != null && otaUpgradeCharacteristic != null) {
            writerOperation.send_data(
                type,
                address,
                buffer,
                length,
                otaUpgradeCharacteristic!!,
                mGatt
            )
        } else {
            false
        }
    }

    fun writeLongCommand(type: Int, address: Int, buffer: ByteArray?, length: Long): Boolean {
        return if (mGatt != null && otaUpgradeCharacteristic != null) {
            writerOperation.send_data_long(
                type,
                address,
                buffer,
                length,
                otaUpgradeCharacteristic!!,
                mGatt
            )
        } else {
            false
        }
    }

    fun requestMtu(mtu: Int) {
        if (mGatt != null) {
            mGatt!!.requestMtu(mtu)
        }
    }

    fun setBleOTANotify() {
        if (mGatt != null && otaNotifyDescriptor != null) {
            mGatt!!.setCharacteristicNotification(otaNotifyCharacteristic, true)
            otaNotifyDescriptor!!.value =
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            mGatt!!.writeDescriptor(otaNotifyDescriptor)
        }
    }

    /**
     * 连接状态改变
     */
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (status != 133) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> {
                    ringDeviceInteractionProtocol.onConnecting(getTargetDeviceMacAddress())
                }
                BluetoothProfile.STATE_CONNECTED -> {
                    connectState = true
                    //发现服务
                    target = TARGET_INIT
                    gatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTING -> {
                    ringDeviceInteractionProtocol.onDisconnecting(getTargetDeviceMacAddress())
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectState = false
                    target = TARGET_INIT
                    ringDeviceInteractionProtocol.onDisconnected(getTargetDeviceMacAddress())
                    resetServer()
                    //断开连接之后 关闭服务
                    closeServer()
                }
            }
        } else {
            //133异常状态重试操作
            Logger.log("133异常状态,重试操作：$target ---")
            if (target == TARGET_DISCONNECTED) {
                runBlocking {
                    delay(2000)
                    disConnect()
                }
            } else if (target == TARGET_CONNECTED) {
                runBlocking {
                    mGatt?.disconnect()
                    delay(2000)
                    mGatt =
                        device?.connectGatt(
                            SDKManager.getApp(), false, this@RingDeviceConnectedManager,
                            BluetoothDevice.TRANSPORT_LE
                        )
                }
            }

        }
    }

    /**
     * 发现服务
     */
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        Logger.log("发现服务---")
        if (gatt != null && status == BluetoothGatt.GATT_SUCCESS) { //BLE服务发现成功
            // 遍历获取BLE服务Services/Characteristics/Descriptors的全部UUID
            for (service in gatt.services) {
                for (characteristic in service.characteristics) {
                    if (UUID_NOTIFY_DATA == characteristic.uuid.toString()) {
                        Logger.log("绑定常规消息服务---")
                        //读及notify特征值
                        ringNotifyDescriptor =
                            characteristic.getDescriptor(UUID.fromString(UUID_DES))
                        if (ringNotifyDescriptor != null) {
                            gatt.setCharacteristicNotification(characteristic, true)
                            ringNotifyDescriptor!!.value =
                                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            gatt.writeDescriptor(ringNotifyDescriptor)
                        }
                    }

                    if (UUID_RING_DATA == characteristic.uuid.toString()) {
                        Logger.log("绑定常规指令写入服务---")
                        //读及notify特征值
                        ringWriteCharacteristic = characteristic
                    }

                    if ((characteristic.uuid.toString() == UUID_SEND_DATA) || (characteristic.uuid.toString() == UUID_SEND_DATA_H)) {
                        Logger.log("绑定OTA指令写入服务---")
                        otaUpgradeCharacteristic = characteristic
                    }

                    if ((characteristic.uuid.toString() == UUID_RECV_DATA) || (characteristic.uuid.toString() == UUID_RECV_DATA_H)) {
                        Logger.log("绑定OTA消息服务---")
                        otaNotifyCharacteristic = characteristic
                        otaNotifyDescriptor =
                            characteristic.getDescriptor(UUID.fromString(UUID_DES))
                    }
                }
            }
            mGatt = gatt
            connectState = true
            ringDeviceInteractionProtocol.onConnected(getTargetDeviceMacAddress())
        } else {
            Logger.log("发现服务异常,即将断开连接---")
            disConnect()
        }
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        ringDeviceInteractionProtocol.onCharacteristicChanged(characteristic?.value)
    }


    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        ringDeviceInteractionProtocol.onMtuChanged(status, mtu)
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        ringDeviceInteractionProtocol.onCharacteristicWrite(status)
    }

    /**
     * 关闭服务
     * 清除数据
     */
    private fun closeServer() {
        mGatt?.close()
        mGatt = null
    }

    /**
     * 重置相关服务
     */
    private fun resetServer() {
        ringNotifyDescriptor = null
        ringWriteCharacteristic = null
        otaUpgradeCharacteristic = null
        otaNotifyDescriptor = null
        otaNotifyCharacteristic = null
    }

}