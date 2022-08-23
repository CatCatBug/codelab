package cc.fastcv.ble.sdk.ring

import android.content.Context
import android.text.format.DateFormat
import cc.fastcv.ble.sdk.SDKManager
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_GET_DEVICE_STATE_INFO
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_BAT
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_CHANTING_REMINDER
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_DATA_RECEIVE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_FIND
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_GET_DEV_COUNT
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_GET_DEV_LOG
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_GET_DEV_SETTING
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_INFO
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_PRAYER_REMINDER
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_RESTART
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_REVERSE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_SETTING_CUSTOMIZE_CONVENTION
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_SETTING_LANGUAGE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_SETTING_STABLE_CONVENTION
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_SET_DEV_BRIGHTNESS
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_SHUTDOWN
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_TIME
import java.text.SimpleDateFormat
import java.util.*

/**
 * 戒指指令生成器
 */
class RingCommandBuilder {

    /**
     * 1.时间下发
     * P01,时间段,12/24小时制
     */
    fun buildP01Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        //12/24小时制：1,0(默认24)
        val fs: String = if (is24HourFormat(SDKManager.getApp())) {
            String.format(Locale.ENGLISH, "$APP_ID_TIME,%s", "$timestamp,0")
        } else {
            String.format(Locale.ENGLISH, "$APP_ID_TIME,%s", "$timestamp,1")
        }
        return fs
    }

    /**
     * 2.获取电量
     * P02,时间段
     */
    fun buildP02Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_BAT,%s", timestamp)
    }

    /**
     * 3.设置翻转：(0:正，1:反)
     * P03,时间段
     */
    fun buildP03Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_REVERSE,%s", timestamp)
    }

    /**
     * 4.寻找设备：操作设置值(0:停止；1:开启)
     * P04,时间段,操作设置值
     */
    fun buildP04Command(enable: Boolean): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        //操作设置值(0:停止；1:开启)
        val fs = if (enable) {
            String.format(Locale.ENGLISH, "$APP_ID_FIND,%s,%s", timestamp, "1")
        } else {
            String.format(Locale.ENGLISH, "$APP_ID_FIND,%s,%s", timestamp, "0")
        }
        return fs
    }

    /**
     * 5.接收到设备状态信息
     * P05,时间段
     */
    fun buildP05Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_GET_DEVICE_STATE_INFO,%s", timestamp)
    }

    /**
     * 6.接收到计数信息
     * P06,时间段,tick值
     */
    fun buildP06Command(tick: String): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_INFO,%s,$tick", timestamp)
    }

    /**
     * 7.实时计数数据接收
     * P07,时间段,tick值
     */
    fun buildP07Command(tick: String): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(
            Locale.ENGLISH,
            "$APP_ID_DATA_RECEIVE,%s,$tick",
            timestamp
        )
    }

    /**
     * 8.远程关机
     * P08,时间段
     */
    fun buildP08Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_SHUTDOWN,%s", timestamp)
    }

    /**
     * 9.祈祷设置
     * P09,时间段,开关,[惯例设置1，惯例设置2，维度，经度，时区]
     */
    fun buildP09Command(
        isOpen: Boolean, calculationMethodPosition: Int, juristicMethodPosition: Int,
        latitude: Double, longitude: Double, timeZone: Double
    ): String {
        val stringBuild = StringBuilder()
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        val fs: String = String.format(Locale.ENGLISH, "$APP_ID_PRAYER_REMINDER,%s", timestamp)

        stringBuild.append("$fs,")
        if (isOpen) {
            stringBuild.append("1,")
            stringBuild.append("$calculationMethodPosition,")
            stringBuild.append("$juristicMethodPosition,")
            val internalLatitude = String.format(Locale.US, "%.5f", latitude)
            val internalLongitude = String.format(Locale.US, "%.5f", longitude)
            stringBuild.append("$internalLatitude,$internalLongitude,$timeZone")
        } else {
            stringBuild.append("0")
        }
        return stringBuild.toString()
    }

    /**
     * 10.重启
     * P10,时间段,蓝牙物理地址(12位)
     */
    fun buildP10Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_RESTART,%s", timestamp)
    }

    /**
     * 11.诵经提醒设置 //时间范围00:00#23:59 //时间间隔 0,30,60,90 0:停止提醒
     * P11,时间段,时间间隔(分钟),时间范围
     * P11,时间段,0
     */
    fun buildP11Command(
        isOpen: Boolean, startTime: String, endTime: String,
        intervalTime: Int
    ): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        val fs: String = String.format(Locale.ENGLISH, "$APP_ID_CHANTING_REMINDER,%s", timestamp)

        val timeInterval = if (!isOpen) {
            "0"
        } else {
            (intervalTime * 60).toString()
        }

        val stringBuilder = StringBuilder()
        stringBuilder.append(fs)
        stringBuilder.append(",")
        stringBuilder.append(timeInterval)

        if (isOpen) {
            stringBuilder.append(",")
            stringBuilder.append("$startTime#$endTime")
        }
        return stringBuilder.toString()
    }

    /**
     * 12.获取设备端所有已存未上传计数数据
     * P12,时间段
     */
    fun buildP12Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_GET_DEV_COUNT,%s", timestamp)
    }

    /**
     * 13.获取设备端设置信息
     * P13,时间段
     */
    fun buildP13Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_GET_DEV_SETTING,%s", timestamp)
    }

    /**
     * 14.获取设备端相关log信息
     * P14,时间段
     */
    fun buildP14Command(): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_GET_DEV_LOG,%s", timestamp)
    }

    /**
     * 15.亮度设置
     * P15,时间段
     */
    fun buildP15Command(brightness: Int): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_SET_DEV_BRIGHTNESS,%s,$brightness", timestamp)
    }

    /**
     * 16.设置戒指语言
     * P16,时间段
     */
    fun buildP16Command(languageType: Int): String {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(Locale.ENGLISH, "$APP_ID_SETTING_LANGUAGE,%s,$languageType", timestamp)
    }

    /**
     * 17.设置戒指自定义惯例
     * P17,时间段,自定义ID,fajr_angle,maghrib_is_minutes,maghrib_value,isha_is_minutes,isha_value
     */
    fun buildP17Command(fajrAngle: Float, ishaIsMinutes: Boolean, ishaValue: Float): String {
        val ishaIsMinutesValue = if (ishaIsMinutes) {
            1
        } else {
            0
        }
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(
            Locale.ENGLISH,
            "$APP_ID_SETTING_CUSTOMIZE_CONVENTION,%s,0,%d,1,0,%d,%d",
            timestamp,
            (fajrAngle * 10).toInt(),
            ishaIsMinutesValue,
            (ishaValue * 10).toInt()
        )
    }

    /**
     * 18.特殊祈祷设置
     * P18,时间段,开关
     * ex:P18,时间段,1
     *    P18,时间段,0
     */
    fun buildP18Command(switch: Boolean): String {
        val isOpen = if (switch) {
            1
        } else {
            0
        }
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())
        return String.format(
            Locale.ENGLISH,
            "$APP_ID_SETTING_STABLE_CONVENTION,%s,%d",
            timestamp,
            isOpen
        )
    }

    /**
     * 判断当前是否是24小时制的时间格式
     */
    private fun is24HourFormat(context: Context = SDKManager.getApp()): Boolean {
        return DateFormat.is24HourFormat(context)
    }

}