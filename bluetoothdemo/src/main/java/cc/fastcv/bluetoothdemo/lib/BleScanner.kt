package cc.fastcv.bluetoothdemo.lib

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import cc.fastcv.bluetoothdemo.ble.BleDev
import java.util.*


@SuppressLint("MissingPermission")
class BleScanner(private val callback: BTCallback) {

    companion object {
        private const val TAG = "BleScanner"
    }

    private val mScanCallback: ScanCallback = object : ScanCallback() {
        // 扫描Callback
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val dev = BleDev(result.device, result)
            callback.onFoundDev(dev)
        }
    }

    fun startScan(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "蓝牙扫描权限未开启，无法使用扫描功能")
            return
        }

        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        Log.d(TAG, "开始搜素设备---------")

        val bluetoothLeScanner = manager.adapter.bluetoothLeScanner
        // Android5.0新增的扫描API，扫描返回的结果更友好，比如BLE广播数据以前是byte[] scanRecord，而新API帮我们解析成ScanRecord类
        bluetoothLeScanner.startScan(mScanCallback)
    }

    fun stopScan(context: Context) {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        Log.d(TAG, "停止搜素设备---------")

        val bluetoothLeScanner = manager.adapter.bluetoothLeScanner
        // Android5.0新增的扫描API，扫描返回的结果更友好，比如BLE广播数据以前是byte[] scanRecord，而新API帮我们解析成ScanRecord类
        bluetoothLeScanner.stopScan(mScanCallback)
    }


    interface BTCallback {
        fun onFoundDev(dev: BleDev)
    }
}