package cc.fastcv.bluetoothdemo.bt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import cc.fastcv.bluetoothdemo.lib.Util

@SuppressLint("MissingPermission")
class BtServer(listener: Listener) : BtBase(listener) {

    companion object {
        private const val TAG = "BtServer"
    }

    private var mSSocket: BluetoothServerSocket? = null

    private var isAccept = false

    /**
     * 监听客户端发起的连接
     */

    fun listen() {
        synchronized(isAccept) {
            try {
                if (!isAccept) {
                    isAccept = true
                } else {
                    Log.d(TAG, "listen: 已处于监听状态----")
                    return
                }
                val adapter = BluetoothAdapter.getDefaultAdapter()
                mSSocket = adapter.listenUsingRfcommWithServiceRecord(TAG, SPP_UUID); //加密传输，Android强制执行配对，弹窗显示配对码
                Log.d(TAG, "listen: 等待连接----")
//                mSSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(
//                    TAG,
//                    SPP_UUID
//                ) //明文传输(不安全)，无需配对
                // 开启子线程
                Util.EXECUTOR.execute {
                    try {
                        val socket: BluetoothSocket = mSSocket!!.accept() // 监听连接
                        mSSocket!!.close() // 关闭监听，只连接一个设备
                        loopRead(socket) // 循环读取
                    } catch (e: Throwable) {
                        close()
                    }
                }
            } catch (e: Throwable) {
                close()
            }
        }
    }

    override fun close() {
        synchronized(isAccept) {
            super.close()
            try {
                mSSocket!!.close()
                isAccept = false
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}