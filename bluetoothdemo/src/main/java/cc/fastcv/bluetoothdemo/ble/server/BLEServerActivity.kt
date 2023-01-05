package cc.fastcv.bluetoothdemo.ble.server

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.os.Bundle
import android.os.ParcelUuid
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.bluetoothdemo.APP
import cc.fastcv.bluetoothdemo.R
import java.util.*

@SuppressLint("MissingPermission")
class BLEServerActivity : AppCompatActivity() {

    companion object {
        val UUID_SERVICE: UUID = UUID.fromString("10000000-0000-0000-0000-000000000000") //自定义UUID

        val UUID_CHAR_READ_NOTIFY: UUID = UUID.fromString("11000000-0000-0000-0000-000000000000")
        val UUID_DESC_NOTITY: UUID = UUID.fromString("11100000-0000-0000-0000-000000000000")
        val UUID_CHAR_WRITE: UUID = UUID.fromString("12000000-0000-0000-0000-000000000000")

        const val TAG = "BLEServerActivity"
    }


    private lateinit var mTips: TextView

    // BLE广播
    private lateinit var mBluetoothLeAdvertiser: BluetoothLeAdvertiser

    // BLE服务端
    private lateinit var mBluetoothGattServer: BluetoothGattServer

    // BLE广播Callback
    private val mAdvertiseCallback: AdvertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            logTv("BLE广播开启成功")
        }

        override fun onStartFailure(errorCode: Int) {
            logTv("BLE广播开启失败,错误码:$errorCode")
        }
    }

    // BLE服务端Callback
    private val mBluetoothGattServerCallback: BluetoothGattServerCallback =
        object : BluetoothGattServerCallback() {
            override fun onConnectionStateChange(
                device: BluetoothDevice,
                status: Int,
                newState: Int
            ) {
                Log.i(
                    TAG,
                    String.format(
                        "onConnectionStateChange:%s,%s,%s,%s",
                        device.name,
                        device.address,
                        status,
                        newState
                    )
                )
                logTv(
                    String.format(
                        if (status == 0) if (newState == 2) "与[%s]连接成功" else "与[%s]连接断开" else "与[%s]连接出错,错误码:$status",
                        device
                    )
                )
            }

            override fun onServiceAdded(status: Int, service: BluetoothGattService) {
                Log.i(
                    TAG,
                    String.format("onServiceAdded:%s,%s", status, service.uuid)
                )
                logTv(
                    String.format(
                        if (status == 0) "添加服务[%s]成功" else "添加服务[%s]失败,错误码:$status",
                        service.uuid
                    )
                )
            }

            override fun onCharacteristicReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                characteristic: BluetoothGattCharacteristic
            ) {
                Log.i(
                    TAG,
                    String.format(
                        "onCharacteristicReadRequest:%s,%s,%s,%s,%s",
                        device.name,
                        device.address,
                        requestId,
                        offset,
                        characteristic.uuid
                    )
                )
                val response = "CHAR_" + (Math.random() * 100).toInt() //模拟数据
                mBluetoothGattServer.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    response.toByteArray()
                ) // 响应客户端
                logTv(
                    """
                客户端读取Characteristic[${characteristic.uuid}]:
                $response
                """.trimIndent()
                )
            }

            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                requestBytes: ByteArray
            ) {
                // 获取客户端发过来的数据
                val requestStr = String(requestBytes)
                Log.i(
                    TAG, String.format(
                        "onCharacteristicWriteRequest:%s,%s,%s,%s,%s,%s,%s,%s",
                        device.name,
                        device.address,
                        requestId,
                        characteristic.uuid,
                        preparedWrite,
                        responseNeeded,
                        offset,
                        requestStr
                    )
                )
                mBluetoothGattServer.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    requestBytes
                ) // 响应客户端
                logTv(
                    """
                客户端写入Characteristic[${characteristic.uuid}]:
                $requestStr
                """.trimIndent()
                )
            }

            override fun onDescriptorReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                descriptor: BluetoothGattDescriptor
            ) {
                Log.i(
                    TAG,
                    String.format(
                        "onDescriptorReadRequest:%s,%s,%s,%s,%s",
                        device.name,
                        device.address,
                        requestId,
                        offset,
                        descriptor.uuid
                    )
                )
                val response = "DESC_" + (Math.random() * 100).toInt() //模拟数据
                mBluetoothGattServer.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    response.toByteArray()
                ) // 响应客户端
                logTv(
                    """
                客户端读取Descriptor[${descriptor.uuid}]:
                $response
                """.trimIndent()
                )
            }

            override fun onDescriptorWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                descriptor: BluetoothGattDescriptor,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                // 获取客户端发过来的数据
                val valueStr = value.contentToString()
                Log.i(
                    TAG, String.format(
                        "onDescriptorWriteRequest:%s,%s,%s,%s,%s,%s,%s,%s",
                        device.name,
                        device.address,
                        requestId,
                        descriptor.uuid,
                        preparedWrite,
                        responseNeeded,
                        offset,
                        valueStr
                    )
                )
                mBluetoothGattServer.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    value
                ) // 响应客户端
                logTv(
                    """
                客户端写入Descriptor[${descriptor.uuid}]:
                $valueStr
                """.trimIndent()
                )

                // 简单模拟通知客户端Characteristic变化
                if (Arrays.toString(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) == valueStr) { //是否开启通知
                    val characteristic = descriptor.characteristic
                    Thread {
                        for (i in 0..4) {
                            SystemClock.sleep(3000)
                            val response = "CHAR_" + (Math.random() * 100).toInt() //模拟数据
                            characteristic.setValue(response)
                            mBluetoothGattServer.notifyCharacteristicChanged(
                                device,
                                characteristic,
                                false
                            )
                            logTv(
                                """
                            通知客户端改变Characteristic[${characteristic.uuid}]:
                            $response
                            """.trimIndent()
                            )
                        }
                    }.start()
                }
            }

            override fun onExecuteWrite(device: BluetoothDevice, requestId: Int, execute: Boolean) {
                Log.i(
                    TAG,
                    String.format(
                        "onExecuteWrite:%s,%s,%s,%s",
                        device.name,
                        device.address,
                        requestId,
                        execute
                    )
                )
            }

            override fun onNotificationSent(device: BluetoothDevice, status: Int) {
                Log.i(
                    TAG,
                    String.format(
                        "onNotificationSent:%s,%s,%s",
                        device.name,
                        device.address,
                        status
                    )
                )
            }

            override fun onMtuChanged(device: BluetoothDevice, mtu: Int) {
                Log.i(
                    TAG,
                    String.format("onMtuChanged:%s,%s,%s", device.name, device.address, mtu)
                )
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_server)

        mTips = findViewById(R.id.tv_tips)

        initServer()
    }


    private fun initServer() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        //        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        val bluetoothAdapter = bluetoothManager.adapter

        // ============启动BLE蓝牙广播(广告) =================================================================================
        //广播设置(必须)

        // ============启动BLE蓝牙广播(广告) =================================================================================
        //广播设置(必须)
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY) //广播模式: 低功耗,平衡,低延迟
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH) //发射功率级别: 极低,低,中,高
            .setConnectable(true) //能否连接,广播分为可连接广播和不可连接广播
            .build()

        //广播数据(必须，广播启动就会发送)
        //广播数据(必须，广播启动就会发送)
        val advertiseData = AdvertiseData.Builder()
            .setIncludeDeviceName(true) //包含蓝牙名称
            .setIncludeTxPowerLevel(true) //包含发射功率级别
            .addManufacturerData(1, byteArrayOf(23, 33)) //设备厂商数据，自定义
            .build()

        //扫描响应数据(可选，当客户端扫描时才发送)
        val scanResponse = AdvertiseData.Builder()
            .addManufacturerData(2, byteArrayOf(66, 66)) //设备厂商数据，自定义
            .addServiceUuid(ParcelUuid(UUID_SERVICE)) //服务UUID
            //                .addServiceData(new ParcelUuid(UUID_SERVICE), new byte[]{2}) //服务数据，自定义
            .build()

        mBluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser
        mBluetoothLeAdvertiser.startAdvertising(
            settings,
            advertiseData,
            scanResponse,
            mAdvertiseCallback
        )

        // 注意：必须要开启可连接的BLE广播，其它设备才能发现并连接BLE服务端!
        // =============启动BLE蓝牙服务端=====================================================================================


        // 注意：必须要开启可连接的BLE广播，其它设备才能发现并连接BLE服务端!
        // =============启动BLE蓝牙服务端=====================================================================================
        val service = BluetoothGattService(
            UUID_SERVICE,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )
        //添加可读+通知characteristic
        //添加可读+通知characteristic
        val characteristicRead = BluetoothGattCharacteristic(
            UUID_CHAR_READ_NOTIFY,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ
        )
        characteristicRead.addDescriptor(
            BluetoothGattDescriptor(
                UUID_DESC_NOTITY,
                BluetoothGattCharacteristic.PERMISSION_WRITE
            )
        )
        service.addCharacteristic(characteristicRead)
        //添加可写characteristic
        val characteristicWrite = BluetoothGattCharacteristic(
            UUID_CHAR_WRITE,
            BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE
        )
        service.addCharacteristic(characteristicWrite)
        mBluetoothGattServer =
            bluetoothManager.openGattServer(this, mBluetoothGattServerCallback)
        mBluetoothGattServer.addService(service)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBluetoothLeAdvertiser.stopAdvertising(
            mAdvertiseCallback
        )
        mBluetoothGattServer.close()
    }

    private fun logTv(msg: String) {
        if (isDestroyed) return
        runOnUiThread {
            APP.toast(msg, 0)
            mTips.append(msg.trimIndent())
        }
    }
}