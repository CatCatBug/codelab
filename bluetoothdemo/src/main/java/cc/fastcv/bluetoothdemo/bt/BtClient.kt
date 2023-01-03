package cc.fastcv.bluetoothdemo.bt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import cc.fastcv.bluetoothdemo.lib.Util

@SuppressLint("MissingPermission")
class BtClient(listener: Listener) : BtBase(listener) {
    /**
     * 与远端设备建立长连接
     *
     * @param dev 远端设备
     */

    fun connect(dev: BluetoothDevice) {
        close()
        try {
             val socket = dev.createRfcommSocketToServiceRecord(SPP_UUID); //加密传输，Android系统强制配对，弹窗显示配对码
//            val socket = dev.createInsecureRfcommSocketToServiceRecord(SPP_UUID) //明文传输(不安全)，无需配对
            // 开启子线程
            Util.EXECUTOR.execute {
                loopRead(socket) //循环读取
            }
        } catch (e: Throwable) {
            close()
        }
    }
}