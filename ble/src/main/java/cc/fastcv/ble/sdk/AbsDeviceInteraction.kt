package cc.fastcv.ble.sdk

import android.os.Handler
import android.os.HandlerThread

/**
 * 与设备连接最近的地方
 * 如果是自己实现的话，则是与bleAPI调用最近的地方
 * 如果是sdk的话，则是与sdk最近的地方
 */
abstract class AbsDeviceInteraction(name: String) :
    HandlerThread(name) {

    abstract fun disConnectDevice()

    abstract fun connectDevice(macAddress: String)

    abstract fun cancelConnect()

    /**
     * 新增处理
     */
    protected fun sendMessage(runnable: Runnable) {
        Handler(looper).post(runnable)
    }

    protected fun sendMessageDelayed(runnable: Runnable, delayMillis : Long) {
        Handler(looper).postDelayed(runnable,delayMillis)
    }

    /**
     * 移除任务
     */
    protected fun removeMessage(runnable: Runnable) {
        Handler(looper).removeCallbacks(runnable)
    }
}