package cc.fastcv.bluetoothdemo.lib

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


object BluetoothTools {

    /**
     * 是否支持低功耗蓝牙
     */
    fun isSupportBLE(context: Context): Boolean {
        val pm: PackageManager = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    }

    /**
     * 是否支持经典蓝牙
     */
    fun isSupportBT(context: Context): Boolean {
        val pm: PackageManager = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    }

    fun getBluetoothPermissionArray() : Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_ADVERTISE)
    } else {
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * 获取本机蓝牙名称
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceName(context: Context) : String? {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter.name
    }

    /**
     * 获取本机蓝牙地址
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceAddress(context: Context) : String? {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter.address
    }


    @SuppressLint("MissingPermission")
    fun discoverable(context: Context) {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        if (manager.adapter.isEnabled) {
            if (manager.adapter.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
                context.startActivity(discoverableIntent)
            }

        }
    }

    /**
     * 创建或者取消匹配
     *
     * @param type 处理类型 1 匹配  2  取消匹配
     * @param device 设备
     */
    private fun createOrRemoveBond(type: Int, device: BluetoothDevice) {
        var method: Method? = null
        try {
            when (type) {
                1 -> {
                    method = BluetoothDevice::class.java.getMethod("createBond")
                    method.invoke(device)
                }
                2 -> {
                    method = BluetoothDevice::class.java.getMethod("removeBond")
                    method.invoke(device)
//                    list.remove(device) //清除列表中已经取消了配对的设备
                }
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }


}