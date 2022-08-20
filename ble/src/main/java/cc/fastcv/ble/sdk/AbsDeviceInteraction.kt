package cc.fastcv.ble.sdk

import android.os.Handler
import android.os.HandlerThread

abstract class AbsDeviceInteraction(name:String) : HandlerThread(name) {

    abstract fun disConnectDevice()

    abstract fun connectDevice(macAddress : String)

    /**
     * 新增处理
     */
    private fun sendMessage(runnable: Runnable) {
        Handler(looper).post(runnable)
    }
}