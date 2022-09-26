package cc.fastcv.codelab.date_switch_view

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    /**
     * 转换 日期时间字符 为 时间戳
     */
    fun convertDateTimeStrToTimestamp(
        dateTimeStr: String,
        simpleDateFormat: SimpleDateFormat
    ): Long {
        val dateTime = simpleDateFormat.parse(dateTimeStr)
        return dateTime?.time ?: 0L
    }

    /**
     * 获取某个日期是周几
     */
    fun getWeekIndexByDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK) - 1
    }

    /**
     * 解析日期时间字符 为 日期 Date
     */
    fun parseDateTimeStrToDate(dateStr: String, simpleDateFormat: SimpleDateFormat): Date {
        return simpleDateFormat.parse(dateStr)!!
    }

    /**
     * 获取指定日期之前/之后amount天的日期
     * 如 amount = 2 currentDate_yMd = "2022-06-30"
     * 得到的结果就是  “2022-07-02”
     */
    fun getOffsetDateByDay(
        amount: Int,
        currentDate_yMd: String,
        dateFormat: SimpleDateFormat
    ): String {
        val currentDate = dateFormat.parse(currentDate_yMd)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate!!
        calendar.add(Calendar.DAY_OF_MONTH, amount)
        return dateFormat.format(calendar.time)
    }

    /**
     * 获取当前时间日期的指定格式的字符串
     * 如：SimpleDateFormat("yyyy-MM-dd",Locale.CHINA)
     * 得到的结果就是  “2022-06-24”
     */
    fun getCurrentDateTimeStr(simpleDateFormat: SimpleDateFormat): String {
        return simpleDateFormat.format(Date())
    }

    /**
     * 获取两个日期之间相差的天数
     */
    fun getDateDifferenceDay(startDateStr: String, endDateStr: String): Int {
        //统一时分秒 方便后面的计算
        val startDateTime = parseDateTimeStrToDate(
            "$startDateStr 00:00:00",
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        )
        val endDateTime = parseDateTimeStrToDate(
            "$endDateStr 00:00:00",
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        )

        // 获取相差的天数
        val calendar = Calendar.getInstance()
        calendar.time = startDateTime
        val startTimeInMillis = calendar.timeInMillis

        calendar.time = endDateTime
        val endTimeInMillis = calendar.timeInMillis
        val betweenDays = (endTimeInMillis - startTimeInMillis) / (1000L * 3600L * 24L)
        return betweenDays.toInt()
    }

    /**
     * 是否是同一天
     */
    fun isSameDay(o1: Calendar, o2: Calendar): Boolean {
        return o1[Calendar.DAY_OF_MONTH] == o2[Calendar.DAY_OF_MONTH]
                && o1[Calendar.MONTH] == o2[Calendar.MONTH]
                && o1[Calendar.YEAR] == o2[Calendar.YEAR]
    }

    /**
     * 获取两个日期之间相差的月
     */
    fun getDateDifferenceMonth(fromDate: Date, toDate: Date): Int {
        val from = Calendar.getInstance()
        from.time = fromDate
        val to = Calendar.getInstance()
        to.time = toDate
        //只要年月
        val fromYear = from[Calendar.YEAR]
        val fromMonth = from[Calendar.MONTH]
        val toYear = to[Calendar.YEAR]
        val toMonth = to[Calendar.MONTH]
        return toYear * 12 + toMonth - (fromYear * 12 + fromMonth)
    }

    /**
     * 获取指定日期之前/之后amount月的日期
     * 如 amount = 2 currentDate_yMd = "2022-06-30"
     * 得到的结果就是  “2022-08-30”
     */
    fun getOffsetDateByMonth(
        amount: Int,
        currentDate_yMd: String,
        dateFormat: SimpleDateFormat
    ): String {
        val currentDate = dateFormat.parse(currentDate_yMd)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate!!
        calendar.add(Calendar.MONTH, amount)
        return dateFormat.format(calendar.time)
    }

}