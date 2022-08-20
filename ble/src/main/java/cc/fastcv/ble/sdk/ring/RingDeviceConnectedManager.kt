package cc.fastcv.ble.sdk.ring

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import cc.fastcv.ble.sdk.SDKManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@SuppressLint("MissingPermission")
class RingDeviceConnectedManager : BluetoothGattCallback() {

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
    private var connectState : Boolean = false

    /**
     * 连接到设备
     */
    @RequiresApi(Build.VERSION_CODES.M)
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
    private var target : Int = TARGET_INIT

    /**
     * 连接状态改变
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (status != 133) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    //发现服务
                    target = TARGET_INIT
                    gatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectState = false
                    target = TARGET_INIT
                    resetServer()
                    //断开连接之后 关闭服务
                    closeServer()
                }
            }
        } else {
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

    private fun closeServer() {
        mGatt?.close()
        mGatt = null
    }

    private fun resetServer() {
        ringNotifyDescriptor = null
        ringWriteCharacteristic = null
        otaUpgradeCharacteristic = null
        otaNotifyDescriptor = null
        otaNotifyCharacteristic = null
    }


}