package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.log.Logger
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_CHANTING_REMINDER_SETTING
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_GET_DEV_COUNT_RECEIVE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.APP_ID_STOP_FIND_RING
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.AUTO_REQUEST_TIME
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_BAT
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_DATA_RECEIVE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_FIND
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_INFO
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_REVERSE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_SETTING
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_STATUS
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_ID_TIME
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_LOG
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_REBOOT
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_SCREEN_BRIGHTNESS
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_SETTING_CUSTOMIZE_CONVENTION
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_SETTING_LANGUAGE_RECEIVE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DEV_SETTING_RECEIVE
import cc.fastcv.ble.sdk.ring.IRingDeviceProtocol.Companion.DVE_ID_SHUTDOWN
import java.text.SimpleDateFormat
import java.util.*

/**
 * 戒指指令解析器
 */
class RingCommandParser {
    /**
     * APP上层回调转发器
     */
    private var appProtocolImpl: IRingAppProtocol? = null

    fun setMsgReceiver(appProtocolImpl: IRingAppProtocol) {
        this.appProtocolImpl = appProtocolImpl
    }

    fun handlerReceiveResult(result: String) {
        Logger.log("收到指令：$result")
        when (result.substring(0, 3)) {
            DEV_ID_TIME -> {
                appProtocolImpl?.onTimeSyncCallback()
            }
            AUTO_REQUEST_TIME -> {
                appProtocolImpl?.requestTimeSync()
            }
            DEV_ID_BAT -> {
                val strArray = result.split(",")
                val status = strArray[1]
                val bat = strArray[2]
                val version = strArray[3]
                val buildDateTime = strArray[4]
                val voltage = strArray[5]
                val versionName = if (strArray.size > 6) {
                    strArray[6]
                } else {
                    version
                }

                Logger.log(
                    "RingCommandParser-- 解析结果：\n" +
                            "充电状态：${parseBatteryStatus(status)} \n" +
                            "电量值:$bat \n" +
                            "内部版本号:$version \n" +
                            "版本编译时间:$buildDateTime \n" +
                            "电压值:$voltage \n" +
                            "版本名:$versionName ---"
                )
                appProtocolImpl?.onBatteryPowerCallback(status, bat, version, versionName)
            }
            DEV_ID_INFO -> {
                val startTime = convertDateTimeFormat(
                    result.split(",")[1],
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                )
                val endTime = convertDateTimeFormat(
                    result.split(",")[2],
                    SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH),
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                )
                val num = result.split(",")[3].toInt()
                val tick = result.split(",")[4]
                val endFlag = result.split(",")[5].toInt()

                Logger.log(
                    "RingCommandParser-- 解析结果：\n" +
                            "开始时间：$startTime \n" +
                            "结束时间:$endTime \n" +
                            "数量:$num \n" +
                            "tick值:$tick \n" +
                            "结束标志:$endFlag ---"
                )

                appProtocolImpl?.onCacheDataCallback(startTime, endTime, num, tick, endFlag)
            }
            DEV_ID_REVERSE -> {
                val strList = result.split(",")
                //0:正；1:反
                val isPositive = (strList[1] == "0")

                Logger.log("解析结果：屏幕方向：${parsePositive(isPositive)}")
                appProtocolImpl?.onScreenFlipCallback(isPositive)
            }
            DEV_ID_FIND -> {
                appProtocolImpl?.onDeviceFind()
            }
            DEV_ID_STATUS -> {
                appProtocolImpl?.onBatteryPowerChangeCallback()
            }
            DEV_ID_DATA_RECEIVE -> {
                val num = result.split(",")[1].toInt()
                Logger.log("解析结果：实时数量：$num")
                appProtocolImpl?.onRealTimeCountCallback(num)
            }
            DVE_ID_SHUTDOWN -> {
                appProtocolImpl?.onShutdownCallback()
            }
            DEV_ID_SETTING -> {
                appProtocolImpl?.onSettingPrayerRemindersCallback()
            }
            APP_ID_CHANTING_REMINDER_SETTING -> {
                appProtocolImpl?.onChantingRemindersSettingCallback()
            }
            APP_ID_GET_DEV_COUNT_RECEIVE -> {
                appProtocolImpl?.onDataCacheSizeCallback()
            }
            DEV_SETTING_RECEIVE -> {
                val strList = result.split(",")
                //屏设置：(0:正；1:反)
                val isPositive = (strList[1] == "0")
                //祈祷设置：开关#开始时间(不设置：0#0)
                val isOpenPray = strList[2].split("#")[0].toInt()
                //诵经设置：时间间隔(分钟)#时间范围(不设置:0#0#0)
                val timeInterval = strList[3].split("#")[0].toInt()

                var chartingStartTime = ""
                var chartingEndTime = ""
                if (timeInterval != 0) {
                    chartingStartTime = strList[3].split("#")[1]
                    chartingEndTime = strList[3].split("#")[2]

                    if (chartingStartTime.contains("255")) {
                        chartingStartTime = ""
                    }

                    if (chartingEndTime.contains("255")) {
                        chartingEndTime = ""
                    }
                }

                //亮度设置
                //语言设置
                val languageType = strList[5].toInt()

                Logger.log(
                    "解析结果：设备配置：\n" +
                            "屏幕方向： ${parsePositive(isPositive)} \n" +
                            "礼拜提醒状态： ${parseSalahRemindStatus(isOpenPray)} \n" +
                            "赞念提醒状态： ${parseDhikrRemindStatus(timeInterval)} \n" +
                            "赞念提醒开始时间： $chartingStartTime \n" +
                            "赞念提醒结束时间： $chartingEndTime \n" +
                            "戒指语言为： ${parseDeviceLanguage(languageType)}"
                )

                appProtocolImpl?.onDeviceConfigInfoCallback(
                    isPositive,
                    isOpenPray,
                    chartingStartTime,
                    chartingEndTime,
                    languageType,
                    timeInterval
                )
            }
            APP_ID_STOP_FIND_RING -> {
                appProtocolImpl?.requestStopFindDevice()
            }
            DEV_REBOOT -> {
                appProtocolImpl?.onRebootCallback()
            }
            DEV_LOG -> {
                appProtocolImpl?.onDeviceLogCallback()
            }
            DEV_SCREEN_BRIGHTNESS -> {
                appProtocolImpl?.onDeviceScreenBrightnessCallback()
            }
            DEV_SETTING_LANGUAGE_RECEIVE -> {
                appProtocolImpl?.onSettingDeviceLanguageCallback()
            }
            DEV_SETTING_CUSTOMIZE_CONVENTION -> {
                appProtocolImpl?.onSettingCustomizeConventionCallback()
            }
        }
    }

    /**
     * 转换时间格式
     * 如：dateTime  20220624145912    parseDateFormat  yyyyMMddHHmmss   formatDateFormat yyyy-MM-dd HH:mm:ss
     * 得到的结果为：2022-06-24 14：59：12
     * 如果传入的格式解析失败  则会返回 “”字符
     */
    private fun convertDateTimeFormat(
        dateTime: String,
        parseDateFormat: SimpleDateFormat,
        formatDateFormat: SimpleDateFormat
    ): String {
        val parseDate = parseDateFormat.parse(dateTime)
        return if (parseDate != null) {
            return formatDateFormat.format(parseDate)
        } else {
            ""
        }
    }

    /**
     * 根据下标解析设备语言归属
     */
    private fun parseDeviceLanguage(languageType: Int) = when (languageType) {
        0 -> "阿拉伯语"
        1 -> "英语"
        2 -> "法语"
        3 -> "乌尔都语"
        4 -> "土耳其语"
        5 -> "简体中文"
        6 -> "印度尼西亚语"
        7 -> "印地语"
        8 -> "波斯语"
        9 -> "德语"
        10 -> "泰语"
        11 -> "俄语"
        12 -> "马来语"
        else -> "未知语言下标"
    }

    /**
     * 解析赞念提醒状态
     */
    private fun parseDhikrRemindStatus(timeInterval: Int) = when (timeInterval) {
        0 -> "关闭"
        else -> "开启"
    }

    /**
     * 解析礼拜提醒状态
     */
    private fun parseSalahRemindStatus(openPray: Int) = when (openPray) {
        0 -> "关闭"
        1 -> "开启"
        else -> "未知状态"
    }

    /**
     * 解析屏幕翻转状态
     * 0:正；1:反
     */
    private fun parsePositive(positive: Boolean): String = if (positive) {
        "正面"
    } else {
        "反面"
    }

    /**
     * 解析设备电池状态
     * 0：未充电；1：充电；2：充满 3：低电
     */
    private fun parseBatteryStatus(status: String) = when (status) {
        "0" -> "未充电"
        "1" -> "充电中"
        "2" -> "已充满"
        "3" -> "低电量"
        else -> "未知状态"
    }

}