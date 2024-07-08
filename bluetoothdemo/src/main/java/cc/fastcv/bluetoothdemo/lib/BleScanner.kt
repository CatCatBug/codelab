package cc.fastcv.bluetoothdemo.lib

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import cc.fastcv.bluetoothdemo.ble.BleDev
import java.lang.StringBuilder
import java.util.*


@SuppressLint("MissingPermission")
class BleScanner(private val callback: BTCallback) {

    companion object {
        private const val TAG = "BleScanner"
    }

    private val mScanCallback: ScanCallback = object : ScanCallback() {
        // 扫描Callback
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(TAG, "onScanResult: callbackType = $callbackType   result = ${getScanResultInfo(result)}")
            val dev = BleDev(result.device, result)
            result.scanRecord
            callback.onFoundDev(dev)
        }
    }


    private fun getScanResultInfo(result: ScanResult) : String {
        val sb = StringBuilder()
        sb.append("device = ${getBluetoothDevice(result.device)}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("advertisingSid = ${result.advertisingSid}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("dataStatus = ${result.dataStatus}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("isLegacy = ${result.isLegacy}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("isConnectable = ${result.isConnectable}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("periodicAdvertisingInterval = ${result.periodicAdvertisingInterval}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("primaryPhy = ${result.primaryPhy}")
        }
        sb.append("rssi = ${result.rssi}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("secondaryPhy = ${result.secondaryPhy}")
        }
        sb.append("timestampNanos = ${result.timestampNanos}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb.append("txPower = ${result.txPower}")
        }
        sb.append("txPower = ${result.scanRecord}")
        return sb.toString()
    }

    private fun getBluetoothDevice(device: BluetoothDevice) : String {
        val sb = StringBuilder()
        sb.append("address = ${device.address}")
        sb.append("name = ${device.name}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            sb.append("alias = ${device.alias}")
        }
        sb.append("type = ${device.type}")
        sb.append("bondState = ${device.bondState}")
        sb.append("bondState = ${device.bluetoothClass}")
        sb.append("uuids = ${device.uuids?.joinToString()}")
        return sb.toString()
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