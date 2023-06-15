package cc.fastcv.traceapp.pray

import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class BasePrayInfo(
    val prayType: PrayerType,
    val timeStamp: Long
) {
    companion object {

        fun transformToBasePrayInfoList(
            prayTimes: List<String>,
            date: Calendar
        ): MutableList<BasePrayInfo> {
            val list = mutableListOf<BasePrayInfo>()
            val year = date[Calendar.YEAR]
            val month = date[Calendar.MONTH] + 1
            val day = date[Calendar.DATE]
            //prayTime统一是24小时制的
            prayTimes.forEachIndexed { index, s ->
                if (!s.contains(":")) {
                    //不是个正常的时间
                    list.add(BasePrayInfo(getPrayerType(index), -1L))
                } else {
                    val parse =
                        try {
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm",
                                Locale.ENGLISH
                            ).parse("$year-$month-$day $s")?.time ?: -1L
                        } catch (e: Exception) {
                            e.printStackTrace()
                            -1L
                        }
                    list.add(BasePrayInfo(getPrayerType(index), parse))
                }
            }
            return list
        }

        private fun getPrayerType(index: Int): PrayerType {
            return when (index) {
                0 -> PrayerType.FAJR
                1 -> PrayerType.SUNRISE
                2 -> PrayerType.DHUHR
                3 -> PrayerType.ASR
                4 -> PrayerType.SUNSET
                5 -> PrayerType.MAGHRIB
                6 -> PrayerType.ISHA
                else -> PrayerType.MIDNIGHT
            }
        }
    }

    //可提供给子类使用
//    fun getTimeStr(context: Context): String {
//        if (timeStamp < 0L) {
//            return "----"
//        }
//        return if (DateFormat.is24HourFormat(context)) {
//            SimpleDateFormat(
//                "HH:mm",
//                Locale.ENGLISH
//            ).format(Date(timeStamp))
//        } else {
//            SimpleDateFormat(
//                "hh:mm",
//                Locale.ENGLISH
//            ).format(Date(timeStamp)) +
//                    if (isAM(timeStamp)) {
//                        R.string.front_page_am.string
//                    } else {
//                        R.string.front_page_pm.string
//                    }
//        }
//    }
//
//    private fun isAM(time: Long): Boolean {
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = time
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        return hour < 12
//    }

    override fun toString(): String {
        return "BasePrayInfo(prayType = $prayType  timeStamp = $timeStamp   datetime = ${
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.ENGLISH
            ).format(Date(timeStamp))
        })"
    }
}
