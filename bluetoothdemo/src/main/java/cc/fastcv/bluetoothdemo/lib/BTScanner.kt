package cc.fastcv.bluetoothdemo.lib

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat

class BTScanner(private val callback: BtReceiver.BTCallback) {

    companion object {
        private const val TAG = "BTScanner"
    }

    private var btReceiver: BtReceiver? = null

    fun startScan(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "蓝牙扫描权限未开启，无法使用扫描功能")
            return
        }

        if (btReceiver == null) {
            Log.d(TAG, "初始化广播")
            btReceiver = BtReceiver(context, callback)
        }

        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        Log.d(TAG, "开始搜素设备---------")
        manager.adapter.startDiscovery()
    }

}