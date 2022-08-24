package cc.fastcv.ble.sdk.ring

/**
 * APP提供的接口
 */
interface IRingAppProtocol {
    //设备端上发协议数据定义：

    /**
     * 主动请求时间同步
     * R01
     */
    fun requestTimeSync()

    /**
     * 主动停止寻找设备：设备端已停止寻找,不需要APP端反馈
     * R04
     */
    fun requestStopFindDevice()

    /**
     * 1.时间请求同步
     * Q01
     */
    fun onTimeSyncCallback()

    /**
     * 2.上发电量
     * 这个指令仅是APP端获取，设备端不会自动上传。
     * 状态值：0：未充电；1：充电；2：充满 3：低电
     * 版本名：(0.1.2)
     * 内部版本号：整数
     * Q02,充电状态值,电量值,内部版本号,版本编译时间,电压值(供测试用),版本名
     */
    fun onBatteryPowerCallback(status: String, bat: String, version: String, versionName: String)

    /**
     * 3.翻转收到
     * 屏设置：(0:正；1:反)
     * Q03，屏设置状态
     */
    fun onScreenFlipCallback(isPositive: Boolean)

    /**
     * 4.寻找设备找到
     * Q04
     */
    fun onDeviceFind()

    /**
     * 5.上发设备状态信息
     * 这个指令仅设备端上传，在蓝牙连接情况下，充电时每10分钟上发一次，不充电时每小时上发一次。
     * Q05,充电状态值,电量值
     */
    fun onBatteryPowerChangeCallback()

    /**
     * 6.上发计数信息：结束符(0:结束；1:未结束)
     * 接收到P12，自动上发未传计数数据
     * Q06,开始时间，结束时间，计数值，tick值，结束符
     */
    fun onCacheDataCallback(
        startTime: String,
        endTime: String,
        num: Int,
        tick: String,
        endFlag: Int
    )

    /**
     * 7.实时计数数据上传
     * Q07,计数值,tick值
     */
    fun onRealTimeCountCallback(num: Int)

    /**
     * 8.关机状态
     * Q08
     */
    fun onShutdownCallback()

    /**
     * 9.祈祷设置
     * Q09
     */
    fun onSettingPrayerRemindersCallback()

    /**
     * 10.重启
     * Q10
     */
    fun onRebootCallback()

    /**
     * 11.诵经提醒设置
     * Q11
     */
    fun onChantingRemindersSettingCallback()

    /**
     * 12.设备自动上发未传计数数据:未传数据条数(0:不存在数据)
     * Q12,未传数据条数
     */
    fun onDataCacheSizeCallback()

    /**
     * 13.设备上发已设置信息
     * 屏设置：(0:正；1:反)
     * 祈祷设置：开关#开始时间(不设置：0#0)
     * 诵经设置：时间间隔(分钟)#时间范围(不设置:0#0#0)
     * Q13,0,5#20210914152938,120#00:00#23:59,0,0
     * Q13,0,0#0,0#0#0,0,0
     * Q13,屏设置,祈祷设置,诵经设置，亮度设置，语言设置
     */
    fun onDeviceConfigInfoCallback(
        isPositive: Boolean,
        isOpenPray: Int,
        chartingStartTime: String,
        chartingEndTime: String,
        languageType: Int,
        timeInterval: Int
    )

    /**
     * 14.设备上发相关log信息
     * log信息长度：总大小为4096,每次最大上传字节是192,可多次连续上传(每接收到P14)
     * log上传结束：Q14,0
     * Q14,log信息长度,log信息
     */
    fun onDeviceLogCallback()

    /**
     * 15.亮度设置
     * Q15
     */
    fun onDeviceScreenBrightnessCallback()

    /**
     * 16.多语言设置回调
     * Q16
     */
    fun onSettingDeviceLanguageCallback()

    /**
     * 17.自定义惯例设置回调
     * Q17
     */
    fun onSettingCustomizeConventionCallback()
}