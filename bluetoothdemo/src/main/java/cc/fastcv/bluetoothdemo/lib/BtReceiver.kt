package cc.fastcv.bluetoothdemo.lib

import android.annotation.SuppressLint
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

class BtReceiver(context: Context, private val callback: BTCallback) : BroadcastReceiver() {

    companion object {
        private const val TAG = "BtReceiver"
    }

    init {
        Log.d(TAG, "初始化广播服务")
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED) //蓝牙开关状态
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED) //蓝牙开始搜索
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) //蓝牙搜索结束
        filter.addAction(BluetoothDevice.ACTION_FOUND) //蓝牙发现新设备(未配对的设备)
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST) //在系统弹出配对框之前(确认/输入配对码)
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED) //设备配对状态改变
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED) //最底层连接建立
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED) //最底层连接断开
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) //BluetoothAdapter连接状态
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED) //BluetoothHeadset连接状态
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED) //BluetoothA2dp连接状态
        context.registerReceiver(this, filter)
    }


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        Log.i(TAG, "action = $action")
        val dev = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        if (dev != null) {
            Log.i(TAG, "获取设备信息：name = " + dev.name + ", address = " + dev.address)
        }
        when (action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                Log.i(TAG, "STATE: $state")
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                Log.i(TAG, "蓝牙开始搜索")
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                Log.i(TAG, "蓝牙搜索结束")
            }
            BluetoothDevice.ACTION_FOUND -> {
                if (dev == null) {
                    Log.i(TAG, "设备信息为空")
                }
                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MAX_VALUE).toInt()
                Log.i(TAG, "EXTRA_RSSI:$rssi")
                callback.onFoundDev(BleDeviceProxy(dev!!, rssi))
            }
            BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                Log.i(TAG, "在系统弹出配对框之前(确认/输入配对码)")
            }
            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                Log.i(TAG, "BOND_STATE: " + intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0))
            }
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                Log.i(TAG, "最底层连接建立")
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                Log.i(TAG, "最底层连接断开")
            }
            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                Log.i(
                    TAG,
                    "CONN_STATE: " + intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0)
                )
            }
            BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                Log.i(TAG, "CONN_STATE: " + intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, 0))
            }
            BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                Log.i(TAG, "CONN_STATE: " + intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, 0))
            }
        }
    }

    interface BTCallback {
        fun onFoundDev(deviceProxy: BleDeviceProxy)
    }
}