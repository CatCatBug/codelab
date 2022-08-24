package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.ConnectStateChangeCallback

class RingDeviceConnectStateManager : ConnectStateChangeCallback {
    /*******    连接状态回调分发器   ********/
    private val stateChangeCallbacks = mutableListOf<ConnectStateChangeCallback>()

    fun registerStateChangeCallback(callback: ConnectStateChangeCallback) {
        if (!stateChangeCallbacks.contains(callback)) {
            stateChangeCallbacks.add(callback)
        }
    }

    fun unRegisterStateChangeCallback(callback: ConnectStateChangeCallback) {
        if (stateChangeCallbacks.contains(callback)) {
            stateChangeCallbacks.remove(callback)
        }
    }

    override fun onConnecting(macAddress: String) {
        stateChangeCallbacks.forEach {
            it.onConnecting(macAddress)
        }
    }

    override fun onConnected(macAddress: String) {
        stateChangeCallbacks.forEach {
            it.onConnected(macAddress)
        }
    }

    override fun onDisconnecting(macAddress: String) {
        stateChangeCallbacks.forEach {
            it.onDisconnecting(macAddress)
        }
    }

    override fun onDisconnected(macAddress: String) {
        stateChangeCallbacks.forEach {
            it.onDisconnected(macAddress)
        }
    }

    override fun onConnectTimeout(macAddress: String) {
        stateChangeCallbacks.forEach {
            it.onConnectTimeout(macAddress)
        }
    }
}