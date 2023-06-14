package cc.fastcv.traceapp.pray

import android.util.Log
import java.util.*

class PrayTimeCoordinator(private val superPrayTime: PrayTimeExt) {

    companion object {
        private const val ADVANCE_TIME = 1000 * 60 * 10
    }

    fun buildPrayTimeList(
        now: Calendar,
        latitude: Double, longitude: Double, tZone: Double
    ) {
        //根据相关日期 算出对应的祈祷类型和时间戳
        val todayList = superPrayTime.getPrayerTimes(now, latitude, longitude, tZone)
        val todayBaseInfoList = BasePrayInfo.transformToBasePrayInfoList(todayList, now)

        now.add(Calendar.DATE, -1)
        val yesterdayList = superPrayTime.getPrayerTimes(now, latitude, longitude, tZone)
        val yesterdayBaseInfoList = BasePrayInfo.transformToBasePrayInfoList(yesterdayList, now)

        now.add(Calendar.DATE, +2)
        val tomorrowList = superPrayTime.getPrayerTimes(now, latitude, longitude, tZone)
        val tomorrowBaseInfoList = BasePrayInfo.transformToBasePrayInfoList(tomorrowList, now)

        //主要算出昨天的午夜时间 和  今天的午夜时间
        val yesterdayMidnightTimeStamp =
            calcMidnightTimeStamp(
                yesterdayBaseInfoList,
                todayBaseInfoList,
                superPrayTime.asrJuristic
            )
        val todayMidnightTimeStamp =
            calcMidnightTimeStamp(
                todayBaseInfoList,
                tomorrowBaseInfoList,
                superPrayTime.asrJuristic
            )

        //得到昨天和今天的相关时间点
        yesterdayBaseInfoList.add(BasePrayInfo(PrayerType.MIDNIGHT, yesterdayMidnightTimeStamp))
        todayBaseInfoList.add(BasePrayInfo(PrayerType.MIDNIGHT, todayMidnightTimeStamp))

        Log.d("xcl_debug", "yesterdayBaseInfoList: ${yesterdayBaseInfoList.joinToString()}")
        Log.d("xcl_debug", "todayBaseInfoList: ${todayBaseInfoList.joinToString()}")

        now.add(Calendar.DATE, -1)

        //判断当前时间是否还在昨天的某个时刻
        val inYesterday = now.timeInMillis < yesterdayBaseInfoList.last().timeStamp
        Log.d("xcl_debug", "inYesterday = $inYesterday")

        val currentStage = getCurrentStage(
            now, if (inYesterday) {
                yesterdayBaseInfoList
            } else {
                todayBaseInfoList
            }
        )
        Log.d("xcl_debug", "currentStage = $currentStage")
    }

    private fun getCurrentStage(
        now: Calendar,
        list: MutableList<BasePrayInfo>
    ): BasePrayInfo {
        Log.d("xcl_debug", " now.timeInMillis: ${now.timeInMillis}")
        //在日出之前十分钟的范围内都是 晨礼
        if (list[1].timeStamp - ADVANCE_TIME > now.timeInMillis) {
            return list[0]
        }

        //在晌礼之前十分钟的范围内都是 日出
        if (list[2].timeStamp - ADVANCE_TIME > now.timeInMillis) {
            return list[1]
        }

        //在晡礼之前十分钟的范围内都是 晌礼
        if (list[3].timeStamp - ADVANCE_TIME > now.timeInMillis) {
            return list[2]
        }

        //在昏礼之前十分钟的范围内都是 晡礼
        //4 - 5 的时间点是一样的  所以这里写的 4
        if (list[4].timeStamp - ADVANCE_TIME > now.timeInMillis) {
            return list[3]
        }

        //在霄礼之前十分钟的范围内都是 昏礼
        if (list[6].timeStamp - ADVANCE_TIME > now.timeInMillis) {
            return list[4]
        }

        //在午夜之前的范围内都是 昏礼
        if (list[7].timeStamp >= now.timeInMillis) {
            return list[6]
        }

        //如果全部过滤完了还没有  那就有问题了 这里为了不奔溃 直接选择最后一个时刻
        return list[6]
    }

    private fun calcMidnightTimeStamp(
        now: MutableList<BasePrayInfo>,
        next: MutableList<BasePrayInfo>,
        asrJuristic: Int
    ): Long {
        return if (asrJuristic == 0) {
            val dTimeStamp = next[1].timeStamp - now[4].timeStamp
            if (dTimeStamp <= 0L) {
                val instance = Calendar.getInstance()
                instance.timeInMillis = next[4].timeStamp
                instance.set(Calendar.HOUR_OF_DAY, 0)
                instance.set(Calendar.MINUTE, 0)
                instance.timeInMillis
            } else {
                now[4].timeStamp + dTimeStamp / 2
            }
        } else {
            val dTimeStamp = next[0].timeStamp - now[4].timeStamp
            if (dTimeStamp <= 0L) {
                val instance = Calendar.getInstance()
                instance.timeInMillis = next[4].timeStamp
                instance.set(Calendar.HOUR_OF_DAY, 0)
                instance.set(Calendar.MINUTE, 0)
                instance.timeInMillis
            } else {
                now[4].timeStamp + dTimeStamp / 2
            }
        }
    }
}