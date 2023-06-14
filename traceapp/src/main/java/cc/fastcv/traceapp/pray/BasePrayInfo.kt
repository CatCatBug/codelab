package cc.fastcv.traceapp.pray

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

        fun getPrayerType(index: Int): PrayerType {
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

    override fun toString(): String {
        return "BasePrayInfo(prayType = $prayType  timeStamp = $timeStamp   datetime = ${
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.ENGLISH
            ).format(Date(timeStamp))
        })"
    }
}
