package cc.fastcv.bluetoothdemo.ble.client

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.bluetoothdemo.APP
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.ble.server.BLEServerActivity.Companion.UUID_CHAR_READ_NOTIFY
import cc.fastcv.bluetoothdemo.ble.server.BLEServerActivity.Companion.UUID_CHAR_WRITE
import cc.fastcv.bluetoothdemo.ble.server.BLEServerActivity.Companion.UUID_DESC_NOTITY
import cc.fastcv.bluetoothdemo.ble.server.BLEServerActivity.Companion.UUID_SERVICE
import java.util.*

@SuppressLint("MissingPermission")
class BLEDeviceActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "BLEDeviceActivity"

        fun startActivity(context: Context, address: String) {
            context.startActivity(Intent(context, BLEDeviceActivity::class.java).apply {
                putExtra("address", address)
            })
        }
    }


    private var mWriteET: EditText? = null
    private var mTips: TextView? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var isConnected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_device)

        mTips = findViewById(R.id.tv_tips)

        mWriteET = findViewById(R.id.et_write)

        val manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val btConnect = findViewById<Button>(R.id.bt_connect)
        btConnect.setOnClickListener {
            intent?.getStringExtra("address")?.let {
                val remoteDevice = manager.adapter.getRemoteDevice(it)
                closeConn()
                mBluetoothGatt =
                    remoteDevice.connectGatt(this, false, mBluetoothGattCallback) // 连接蓝牙设备

                logTv(String.format("与[%s]开始连接............", remoteDevice.address))
            }
        }

        val btDisConnect = findViewById<Button>(R.id.bt_disconnect)
        btDisConnect.setOnClickListener {
            closeConn()
        }

    }

    // 注意：连续频繁读写数据容易失败，读写操作间隔最好200ms以上，或等待上次回调完成后再进行下次读写操作！
    // 读取数据成功会回调->onCharacteristicChanged()
    fun read(view: View?) {
        val service = getGattService(UUID_SERVICE)
        if (service != null) {
            val characteristic =
                service.getCharacteristic(UUID_CHAR_READ_NOTIFY) //通过UUID获取可读的Characteristic
            mBluetoothGatt!!.readCharacteristic(characteristic)
        }
    }

    // 注意：连续频繁读写数据容易失败，读写操作间隔最好200ms以上，或等待上次回调完成后再进行下次读写操作！
    // 写入数据成功会回调->onCharacteristicWrite()
    fun write(view: View?) {
        val service = getGattService(UUID_SERVICE)
        if (service != null) {
            val text = mWriteET!!.text.toString()
            val characteristic =
                service.getCharacteristic(UUID_CHAR_WRITE) //通过UUID获取可写的Characteristic
            characteristic.value = text.toByteArray() //单次最多20个字节
            mBluetoothGatt!!.writeCharacteristic(characteristic)
        }
    }

    // 设置通知Characteristic变化会回调->onCharacteristicChanged()
    fun setNotify(view: View?) {
        val service = getGattService(UUID_SERVICE)
        if (service != null) {
            // 设置Characteristic通知
            val characteristic =
                service.getCharacteristic(UUID_CHAR_READ_NOTIFY) //通过UUID获取可通知的Characteristic
            mBluetoothGatt!!.setCharacteristicNotification(characteristic, true)

            // 向Characteristic的Descriptor属性写入通知开关，使蓝牙设备主动向手机发送数据
            val descriptor = characteristic.getDescriptor(UUID_DESC_NOTITY)
            // descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);//和通知类似,但服务端不主动发数据,只指示客户端读取数据
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            mBluetoothGatt!!.writeDescriptor(descriptor)
        }
    }

    // 获取Gatt服务
    private fun getGattService(uuid: UUID): BluetoothGattService? {
        if (!isConnected) {
            APP.toast("没有连接", 0)
            return null
        }
        val service = mBluetoothGatt!!.getService(uuid)
        if (service == null) APP.toast("没有找到服务UUID=$uuid", 0)
        return service
    }

    // 输出日志
    private fun logTv(msg: String) {
        if (isDestroyed) return
        runOnUiThread {
            APP.toast(msg, 0)
            mTips!!.append(msg.trimIndent())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeConn()
    }

    // BLE中心设备连接外围设备的数量有限(大概2~7个)，在建立新连接之前必须释放旧连接资源，否则容易出现连接错误133
    private fun closeConn() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt!!.disconnect()
            mBluetoothGatt!!.close()
        }
    }

    // 与服务端连接的Callback
    private var mBluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val dev = gatt.device
            Log.i(
                TAG,
                String.format(
                    "onConnectionStateChange:%s,%s,%s,%s",
                    dev.name,
                    dev.address,
                    status,
                    newState
                )
            )
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                isConnected = true
                gatt.discoverServices() //启动服务发现
            } else {
                isConnected = false
                closeConn()
            }
            logTv(
                String.format(
                    if (status == 0) if (newState == 2) "与[%s]连接成功" else "与[%s]连接断开" else "与[%s]连接出错,错误码:$status",
                    dev
                )
            )
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i(
                TAG,
                String.format(
                    "onServicesDiscovered:%s,%s,%s",
                    gatt.device.name,
                    gatt.device.address,
                    status
                )
            )
            if (status == BluetoothGatt.GATT_SUCCESS) { //BLE服务发现成功
                // 遍历获取BLE服务Services/Characteristics/Descriptors的全部UUID
                for (service in gatt.services) {
                    val allUUIDs = StringBuilder(
                        """
                          UUIDs={
                          S=${service.uuid}
                          """.trimIndent()
                    )
                    for (characteristic in service.characteristics) {
                        allUUIDs.append(",\nC=").append(characteristic.uuid)
                        for (descriptor in characteristic.descriptors) allUUIDs.append(",\nD=")
                            .append(descriptor.uuid)
                    }
                    allUUIDs.append("}")
                    Log.i(
                        TAG,
                        "onServicesDiscovered:$allUUIDs"
                    )
                    logTv("发现服务$allUUIDs")
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            val uuid = characteristic.uuid
            val valueStr = String(characteristic.value)
            Log.i(
                TAG,
                String.format(
                    "onCharacteristicRead:%s,%s,%s,%s,%s",
                    gatt.device.name,
                    gatt.device.address,
                    uuid,
                    valueStr,
                    status
                )
            )
            logTv("读取Characteristic[$uuid]:\n$valueStr")
        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            BluetoothGatt.GATT_FAILURE
            val uuid = characteristic.uuid
            val valueStr = String(characteristic.value)
            Log.i(
                TAG,
                String.format(
                    "onCharacteristicWrite:%s,%s,%s,%s,%s",
                    gatt.device.name,
                    gatt.device.address,
                    uuid,
                    valueStr,
                    status
                )
            )
            logTv("写入Characteristic[$uuid]:\n$valueStr")
        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            val uuid = characteristic.uuid
            val valueStr = String(characteristic.value)
            Log.i(
                TAG,
                String.format(
                    "onCharacteristicChanged:%s,%s,%s,%s",
                    gatt.device.name,
                    gatt.device.address,
                    uuid,
                    valueStr
                )
            )
            logTv("通知Characteristic[$uuid]:\n$valueStr")
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            val uuid = descriptor.uuid
            val valueStr = Arrays.toString(descriptor.value)
            Log.i(
                TAG,
                String.format(
                    "onDescriptorRead:%s,%s,%s,%s,%s",
                    gatt.device.name,
                    gatt.device.address,
                    uuid,
                    valueStr,
                    status
                )
            )
            logTv("读取Descriptor[$uuid]:\n$valueStr")
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            val uuid = descriptor.uuid
            val valueStr = Arrays.toString(descriptor.value)
            Log.i(
                TAG,
                String.format(
                    "onDescriptorWrite:%s,%s,%s,%s,%s",
                    gatt.device.name,
                    gatt.device.address,
                    uuid,
                    valueStr,
                    status
                )
            )
            logTv("写入Descriptor[$uuid]:\n$valueStr")
        }

    }
}