package cc.fastcv.bluetoothdemo.bt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Environment
import android.util.Log
import cc.fastcv.bluetoothdemo.APP
import cc.fastcv.bluetoothdemo.lib.Util
import java.io.*
import java.util.*

@SuppressLint("MissingPermission")
open class BtBase(private var mListener: Listener?) {

    companion object {
        val SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private val FILE_PATH = Environment.getExternalStorageDirectory().absolutePath
        private const val FLAG_FILE = 1 //文件标记
        private const val FLAG_MSG = 0 //消息标记
    }

    internal var mSocket: BluetoothSocket? = null
    private var mOut: DataOutputStream? = null
    internal var isRead = false
    private var isSending = false

    /**
     * 循环读取对方数据(若没有数据，则阻塞等待)
     */
    fun loopRead(socket: BluetoothSocket?) {
        mSocket = socket
        try {
            if (!mSocket!!.isConnected) mSocket!!.connect()
            notifyUI(Listener.CONNECTED, mSocket!!.remoteDevice)
            mOut = DataOutputStream(mSocket!!.outputStream)
            val `in` = DataInputStream(mSocket!!.inputStream)
            isRead = true
            while (isRead) { //死循环读取
                when (`in`.readInt()) {
                    FLAG_MSG -> {
                        val msg = `in`.readUTF()
                        notifyUI(Listener.MSG, "接收短消息：$msg")
                    }
                    FLAG_FILE -> {
                        notifyUI(Listener.MSG, "准备接收文件...")
                        Util.mkdirs(FILE_PATH)
                        val fileName = `in`.readUTF() //文件名
                        val fileLen = `in`.readLong() //文件长度
                        // 读取文件内容
                        var len: Long = 0
                        var r: Int
                        val b = ByteArray(4 * 1024)
                        val out = FileOutputStream("$FILE_PATH/$fileName")
                        notifyUI(Listener.MSG, "正在接收文件($fileName),请稍后...")

                        var progress = 0
                        while (`in`.read(b).also { r = it } != -1) {
                            out.write(b, 0, r)
                            len += r.toLong()
                            val v = (len*1.0f / fileLen*100).toInt()
                            if (v != progress) {
                                progress = v
                                notifyUI(Listener.MSG, "已接收$progress %...")
                            }

                            if (len >= fileLen) break
                        }
                        notifyUI(Listener.MSG, "文件接收完成(存放在:$FILE_PATH)")
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e("xcl_debug", "连接错误", e)
            close()
        }
    }

    /**
     * 发送短消息
     */
    fun sendMsg(msg: String) {
        if (checkSend()) return
        isSending = true
        try {
            mOut!!.writeInt(FLAG_MSG) //消息标记
            mOut!!.writeUTF(msg)
            mOut!!.flush()
            notifyUI(Listener.MSG, "发送短消息：$msg")
        } catch (e: Throwable) {
            close()
        }
        isSending = false
    }

    /**
     * 发送文件
     */
    fun sendFile(filePath: String) {
        if (checkSend()) return
        isSending = true
        Util.EXECUTOR.execute {
            try {
                val `in` = FileInputStream(filePath)
                val file = File(filePath)
                mOut!!.writeInt(FLAG_FILE) //文件标记
                mOut!!.writeUTF(file.name) //文件名
                mOut!!.writeLong(file.length()) //文件长度
                var r: Int
                val b = ByteArray(4 * 1024)
                notifyUI(Listener.MSG, "正在发送文件($filePath),请稍后...")

                val total = file.length()
                var sendSize = 0L
                var progress = 0
                while (`in`.read(b).also { r = it } != -1) {
                    mOut!!.write(b, 0, r)
                    sendSize += r
                    val v = (sendSize*1.0f / total * 100).toInt()
                    if (v != progress) {
                        progress = v
                        notifyUI(Listener.MSG, "已完成$progress %...")
                    }
                }
                mOut!!.flush()
                notifyUI(Listener.MSG, "文件发送完成.")
            } catch (e: Throwable) {
                e.printStackTrace()
                close()
            }
            isSending = false
        }
    }

    /**
     * 释放监听引用(例如释放对Activity引用，避免内存泄漏)
     */
    fun unListener() {
        mListener = null
    }

    /**
     * 关闭Socket连接
     */
    open fun close() {
        try {
            isRead = false
            mSocket!!.close()
            notifyUI(Listener.DISCONNECTED, null)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 当前设备与指定设备是否连接
     */
    fun isConnected(dev: BluetoothDevice?): Boolean {
        val connected = mSocket != null && mSocket!!.isConnected
        return if (dev == null) connected else connected && mSocket!!.remoteDevice == dev
    }

    // ============================================通知UI===========================================================
    private fun checkSend(): Boolean {
        if (isSending) {
            APP.toast("正在发送其它数据,请稍后再发...", 0)
            return true
        }
        return false
    }

    internal fun notifyUI(state: Int, obj: Any?) {
        APP.runUi {
            try {
                if (mListener != null) mListener!!.socketNotify(state, obj)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    interface Listener {
        fun socketNotify(state: Int, obj: Any?)

        companion object {
            const val DISCONNECTED = 0
            const val CONNECTED = 1
            const val MSG = 2
        }
    }
}