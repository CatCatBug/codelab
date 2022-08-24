package cc.fastcv.ble.sdk.ring

class DefaultRingAppProtocolImpl(interaction: RingDeviceInteraction) : IRingAppProtocol {

    init {
        interaction.setMsgReceiver(this)
    }

    /*******    设备回调分发器   ********/
    private val emptyRingAppProtocols = mutableListOf<IEmptyRingAppProtocol>()

    fun registerDeviceCallback(callback: IEmptyRingAppProtocol) {
        if (!emptyRingAppProtocols.contains(callback)) {
            emptyRingAppProtocols.add(callback)
        }
    }

    fun unRegisterDeviceCallback(callback: IEmptyRingAppProtocol) {
        if (emptyRingAppProtocols.contains(callback)) {
            emptyRingAppProtocols.remove(callback)
        }
    }

    override fun requestTimeSync() {
        emptyRingAppProtocols.forEach {
            it.requestTimeSync()
        }
    }

    override fun requestStopFindDevice() {
        emptyRingAppProtocols.forEach {
            it.requestStopFindDevice()
        }
    }

    override fun onTimeSyncCallback() {
        emptyRingAppProtocols.forEach {
            it.onTimeSyncCallback()
        }
    }

    override fun onBatteryPowerCallback(
        status: String,
        bat: String,
        version: String,
        versionName: String
    ) {
        emptyRingAppProtocols.forEach {
            it.onBatteryPowerCallback(status, bat, version, versionName)
        }
    }

    override fun onScreenFlipCallback(isPositive: Boolean) {
        emptyRingAppProtocols.forEach {
            it.onScreenFlipCallback(isPositive)
        }
    }

    override fun onDeviceFind() {
        emptyRingAppProtocols.forEach {
            it.onDeviceFind()
        }
    }

    override fun onBatteryPowerChangeCallback() {
        emptyRingAppProtocols.forEach {
            it.onBatteryPowerChangeCallback()
        }
    }

    override fun onCacheDataCallback(
        startTime: String,
        endTime: String,
        num: Int,
        tick: String,
        endFlag: Int
    ) {
        emptyRingAppProtocols.forEach {
            it.onCacheDataCallback(startTime, endTime, num, tick, endFlag)
        }
    }

    override fun onRealTimeCountCallback(num: Int) {
        emptyRingAppProtocols.forEach {
            it.onRealTimeCountCallback(num)
        }
    }

    override fun onShutdownCallback() {
        emptyRingAppProtocols.forEach {
            it.onShutdownCallback()
        }
    }

    override fun onSettingPrayerRemindersCallback() {
        emptyRingAppProtocols.forEach {
            it.onSettingPrayerRemindersCallback()
        }
    }

    override fun onRebootCallback() {
        emptyRingAppProtocols.forEach {
            it.onRebootCallback()
        }
    }

    override fun onChantingRemindersSettingCallback() {
        emptyRingAppProtocols.forEach {
            it.onChantingRemindersSettingCallback()
        }
    }

    override fun onDataCacheSizeCallback() {
        emptyRingAppProtocols.forEach {
            it.onDataCacheSizeCallback()
        }
    }

    override fun onDeviceConfigInfoCallback(
        isPositive: Boolean,
        isOpenPray: Int,
        chartingStartTime: String,
        chartingEndTime: String,
        languageType: Int,
        timeInterval: Int
    ) {
        emptyRingAppProtocols.forEach {
            it.onDeviceConfigInfoCallback(isPositive, isOpenPray, chartingStartTime, chartingEndTime, languageType, timeInterval)
        }
    }

    override fun onDeviceLogCallback() {
        emptyRingAppProtocols.forEach {
            it.onDeviceLogCallback()
        }
    }

    override fun onDeviceScreenBrightnessCallback() {
        emptyRingAppProtocols.forEach {
            it.onDeviceScreenBrightnessCallback()
        }
    }

    override fun onSettingDeviceLanguageCallback() {
        emptyRingAppProtocols.forEach {
            it.onSettingDeviceLanguageCallback()
        }
    }

    override fun onSettingCustomizeConventionCallback() {
        emptyRingAppProtocols.forEach {
            it.onSettingCustomizeConventionCallback()
        }
    }
}