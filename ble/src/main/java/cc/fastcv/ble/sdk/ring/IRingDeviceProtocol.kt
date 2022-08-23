package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.OTAUpgradeListener
import java.io.File

/**
 * 戒指端的接口
 */
interface IRingDeviceProtocol {
    //APP端下发协议数据定义：

    /**
     * 1.时间下发
     * P01,时间段,12/24小时制
     */
    fun syncCurrentTimeAndTimeFormat()

    /**
     * 2.获取电量
     * P02,时间段
     */
    fun getDeviceBatteryPower()

    /**
     * 3.设置翻转：(0:正，1:反)
     * P03,时间段
     */
    fun flipDeviceScreen()

    /**
     * 4.寻找设备：操作设置值(0:停止；1:开启)
     * P04,时间段,1
     */
    fun startFindDevice()

    /**
     * 4.寻找设备：操作设置值(0:停止；1:开启)
     * P04,时间段,0
     */
    fun stopFindDevice()

    /**
     * 5.接收到设备状态信息
     * P05,时间段
     */
    fun getDeviceState()

    /**
     * 6.接收到计数信息
     * P06,时间段,tick值
     */
    fun getPrayerCountInformation(tick: String)

    /**
     * 7.实时计数数据接收
     * P07,时间段,tick值
     */
    fun getRealTimePrayerCount(tick: String)

    /**
     * 8.远程关机
     * P08,时间段
     */
    fun shutdown()

    /**
     * 9.祈祷设置
     * P09,时间段,开关,[惯例设置1，惯例设置2，维度，经度，时区]
     */
    fun setPrayerRemindersState(
        isOpen: Boolean, calculationMethodPosition: Int, juristicMethodPosition: Int,
        latitude: Double, longitude: Double, timeZone: Double
    ) 

    /**
     * 10.重启
     * P10,时间段,蓝牙物理地址(12位)
     */
    fun reboot()

    /**
     * 11.诵经提醒设置 //时间范围00:00#23:59 //时间间隔 0,30,60,90 0:停止提醒
     * P11,时间段,时间间隔(分钟),时间范围
     * P11,时间段,0
     */
    fun setPrayerReminders(
        isOpen: Boolean, startTime: String, endTime: String,
        intervalTime: Int
    )

    /**
     * 12.获取设备端所有已存未上传计数数据
     * P12,时间段
     */
    fun getDeviceCacheData()

    /**
     * 13.获取设备端设置信息
     * P13,时间段
     */
    fun getDeviceConfigInfo()

    /**
     * 14.获取设备端相关log信息
     * P14,时间段
     */
    fun getDeviceLog()

    /**
     * 15.亮度设置
     * P15,时间段,亮度大小
     * 亮度大小(0,1)，默认0
     */
    fun setDeviceScreenBrightness(brightness: Int)

    /**
     * 16.语言显示设置
     * P16,时间段,语言ID（0-9）
     * 语言ID：阿拉，英文，法语，乌尔都，土耳其，中文，印尼，印地，波斯，德语，默认0
     */
    fun setDeviceLanguage(languageType: Int)

    /**
     * 17.自定义惯例设置
     * P17,时间段,自定义ID,fajr_angle,maghrib_is_minutes,maghrib_value,isha_is_minutes,isha_value
     * 自定义ID:0,1,2
     * isha_is_minutes:当Isha Angle=Time,对应值=1;否则为0
     */
    fun setCustomizeConvention(fajrAngle: Float, ishaIsMinutes: Boolean, ishaValue: Float)

    /**
     * 18.特殊祈祷设置
     * P18,时间段,开关
     * ex:P18,时间段,1
     *    P18,时间段,0
     */
    fun setStableConvention(switch:Boolean)

    /**
     * OTA升级接口
     */
    fun startOTAUpgrade(file: File, listener: OTAUpgradeListener)

    companion object {
        //设备端上发协议数据定义：
        /**
         * 1.时间请求同步： Q01
         */
        const val DEV_ID_TIME = "Q01"

        /**
         * 设备端主动请求时间同步：
         */
        const val AUTO_REQUEST_TIME = "R01"

        /**
         * 2.上发电量：Q02,充电状态值,电量值,内部版本号,版本编译时间,电压值(供测试用),版本名
         */
        const val DEV_ID_BAT = "Q02"

        /**
         * 3.反转收到： Q03
         */
        const val DEV_ID_REVERSE = "Q03"

        /**
         * 4.寻找设备找到： Q04
         */
        const val DEV_ID_FIND = "Q04"

        /**
         * 5.上发设备状态信息： Q05,充电状态值
         */
        const val DEV_ID_STATUS = "Q05"

        /**
         * 6.上发计数信息： Q06,开始时间，结束时间，计数值，tick值
         */
        const val DEV_ID_INFO = "Q06"

        /**
         * 7.实时计数数据上传：Q07,计数值，tick值
         */
        const val DEV_ID_DATA_RECEIVE = "Q07"

        /**
         * 8.关机状态：Q08
         */
        const val DVE_ID_SHUTDOWN = "Q08"

        /**
         * 9.祈祷设置：Q09
         */
        const val DEV_ID_SETTING = "Q09"

        /**
         * 10.重启
         */
        const val DEV_REBOOT = "Q10"

        /**
         * 11 诵经提醒设置：Q11,时间段
         */
        const val APP_ID_CHANTING_REMINDER_SETTING = "Q11"

        /**
         * 12 未传数据条数
         */
        const val APP_ID_GET_DEV_COUNT_RECEIVE = "Q12"

        /**
         * 13 戒指设置项 Q13,屏设置,祈祷设置,诵经设置，亮度设置，语言设置
         */
        const val DEV_SETTING_RECEIVE = "Q13"

        /**
         * 14 日志
         */
        const val DEV_LOG = "Q14"

        /**
         * 屏幕亮度
         */
        const val DEV_SCREEN_BRIGHTNESS = "Q15"

        /**
         * 设置多语言回调
         */
        const val DEV_SETTING_LANGUAGE_RECEIVE = "Q16"

        /**
         * 设置自定义惯例的回调
         */
        const val DEV_SETTING_CUSTOMIZE_CONVENTION = "Q17"

        /**
         * 主动停止寻找设备：设备端已停止寻找,不需要APP端反馈
         */
        const val APP_ID_STOP_FIND_RING = "R04"



        /**
         *APP端下发协议数据定义：
         */
        /**
         * 1.APP端时间下发
         * P01,时间段（20220606110423040）,12/24小时制
         */
        const val APP_ID_TIME = "P01"

        /**
         * 2.获取电量
         * P02,时间段
         */
        const val APP_ID_BAT = "P02"

        /**
         * 3.设置反转
         * P03,时间段，翻转开关
         */
        const val APP_ID_REVERSE = "P03"

        /**
         * 4.寻找设备
         * P04,时间段
         */
        const val APP_ID_FIND = "P04"

        /**
         * 5.接收到设备状态信息
         * P05,时间段
         */
        const val APP_GET_DEVICE_STATE_INFO = "P05"

        /**
         * 6.接收到计数信息
         * P06,时间段，tick值
         */
        const val APP_ID_INFO = "P06"

        /**
         * 7.实时计数数据接收
         * P07,时间段，tick值
         */
        const val APP_ID_DATA_RECEIVE = "P07"

        /**
         * 8.远程关机
         * P08,时间段
         */
        const val APP_ID_SHUTDOWN = "P08"

        /**
         * 9.祈祷设置
         * P09,时间段 PrayerReminder
         */
        const val APP_ID_PRAYER_REMINDER = "P09"

        /**
         * 10.远程重启
         * P10,时间段
         */
        const val APP_ID_RESTART = "P10"

        /**
         * 11.远程重启
         * P10,时间段
         */
        const val APP_ID_CHANTING_REMINDER = "P11"

        /**
         * 12.获取设备端所有已存未上传计数数据
         * P12,时间段
         */
        const val APP_ID_GET_DEV_COUNT = "P12"

        /**
         * 13.获取设备端设置信息
         * P13,时间段
         */
        const val APP_ID_GET_DEV_SETTING = "P13"

        /**
         * 14.获取设备端相关log信息
         * P14,时间段
         */
        const val APP_ID_GET_DEV_LOG = "P14"

        /**
         * 15.亮度设置
         * P15,时间段
         */
        const val APP_ID_SET_DEV_BRIGHTNESS = "P15"

        /**
         * 16.设置戒指语言
         * P16,时间段
         */
        const val APP_ID_SETTING_LANGUAGE = "P16"

        /**
         * 17.设置戒指自定义惯例
         * P17,时间段,自定义ID,fajr_angle,maghrib_is_minutes,maghrib_value,isha_is_minutes,isha_value
         */
        const val APP_ID_SETTING_CUSTOMIZE_CONVENTION = "P17"

        /**
         * 18.特殊祈祷设置
         * P18,时间段,开关
         * ex:P18,时间段,1
         *    P18,时间段,0
         */
        const val APP_ID_SETTING_STABLE_CONVENTION = "P18"
    }
}