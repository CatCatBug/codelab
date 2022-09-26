package cc.fastcv.codelab.date_switch_view

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DateBuildFactory {

    private val dateUtil = DateUtil()

    var startDate: String = ""
    var endDate: String = ""

    fun setDateRange(start: String, end: String) {
        startDate = start
        endDate = end
    }

    fun buildMonthList(): MutableList<WeekInfo> {
        var bindDateStr = startDate

        val endDateStr = endDate
        //获取两个时间节点相差的月数
        val difMonthNumber = dateUtil.getDateDifferenceMonth(
            dateUtil.parseDateTimeStrToDate(bindDateStr, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)),
            dateUtil.parseDateTimeStrToDate(endDateStr, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
        )

        Log.d("xcl_debug", "buildMonthList---------difMonthNumber: $difMonthNumber")

        val toBeAdded = 7 - difMonthNumber % 7
        val startDateStr = dateUtil.getOffsetDateByMonth(
            -toBeAdded, bindDateStr,
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        )
        Log.d("xcl_debug", "buildMonthList: startDateStr = $startDateStr")
        val listTotal: Int = (difMonthNumber + toBeAdded) / 7
        Log.d("xcl_debug", "buildMonthList: listTotal = $listTotal")
        //所有的数据列表
        val list = mutableListOf<WeekInfo>()

        //绑定设备时的时间戳
        val bindDeviceTimestamp =
            dateUtil.convertDateTimeStrToTimestamp(bindDateStr, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
        //获取绑定时间的calendar对象 方便做对比操作
        val bindCalendar = Calendar.getInstance()
        bindCalendar.timeInMillis = bindDeviceTimestamp

        //获取开始日期的calendar对象 方便做日期自加的操作
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(startDateStr)
        val calendar = Calendar.getInstance()
        calendar.time = startDate!!
        //往开始日期前移动一个月，方便后面的计算

        //获取现在的calendar对象 方便做对比操作
        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = Date()

        for (i in 1..listTotal) {
            val weekInfo = WeekInfo()
            for (j in 0..6) {
                calendar.add(Calendar.MONTH, 1)
                val dayInfo = buildDayInfo(calendar, bindCalendar, todayCalendar, 1)
                weekInfo.weekContent[j] = dayInfo
            }
            list.add(weekInfo)
        }
        return list
    }

    private fun buildDayInfo(
        calendar: Calendar,
        bindCalendar: Calendar,
        todayCalendar: Calendar,
        type: Int
    ): DayInfo {
        val dayInfo = DayInfo()
        dayInfo.year = calendar[Calendar.YEAR]
        dayInfo.month = calendar[Calendar.MONTH] + 1
        dayInfo.day = calendar[Calendar.DAY_OF_MONTH]
        dayInfo.isInvalid =
            if (type == 0) {
                (bindCalendar.timeInMillis > calendar.timeInMillis || todayCalendar.timeInMillis < calendar.timeInMillis)
            } else {
                (bindCalendar[Calendar.YEAR] > calendar[Calendar.YEAR]
                        || (bindCalendar[Calendar.YEAR] == calendar[Calendar.YEAR] && bindCalendar[Calendar.MONTH] > calendar[Calendar.MONTH]))
                        || (todayCalendar[Calendar.YEAR] < calendar[Calendar.YEAR]
                        || (todayCalendar[Calendar.YEAR] == calendar[Calendar.YEAR] && todayCalendar[Calendar.MONTH] < calendar[Calendar.MONTH]))
            }

        dayInfo.isToday = if (type == 0) {
            dateUtil.isSameDay(todayCalendar, calendar)
        } else {
            todayCalendar[Calendar.YEAR] == calendar[Calendar.YEAR] && todayCalendar[Calendar.MONTH] == calendar[Calendar.MONTH]
        }
        dayInfo.type = type
        dayInfo.isSelected = dayInfo.isToday
        return dayInfo
    }

    fun buildDayList(): MutableList<WeekInfo> {
        var bindDateStr = startDate
        //获取列表开始时间
        val startDateStr = getListStartDate(bindDateStr)
        //获取列表结束时间
        val endDateStr = getListEndDate()

        Log.d("xcl_debug", "buildDayList: $startDateStr    $endDateStr")

        //获取两个时间节点相差的天数
        val difDayNumber = dateUtil.getDateDifferenceDay(startDateStr, endDateStr) + 1

        Log.d("xcl_debug", "difDayNumber: $difDayNumber")
        //除以7得到总共显示的周数
        val listTotal: Int = difDayNumber / 7
        //所有的数据列表
        val list = mutableListOf<WeekInfo>()

        //绑定设备时的时间戳
        val bindDeviceTimestamp =
            dateUtil.convertDateTimeStrToTimestamp(bindDateStr, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
        //获取绑定时间的calendar对象 方便做对比操作
        val bindCalendar = Calendar.getInstance()
        bindCalendar.timeInMillis = bindDeviceTimestamp

        //获取开始日期的calendar对象 方便做日期自加的操作
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(startDateStr)
        val calendar = Calendar.getInstance()
        calendar.time = startDate!!
        //往开始日期前移动一天，方便后面的计算
        calendar.add(Calendar.DAY_OF_MONTH, -1)

        //获取现在的calendar对象 方便做对比操作
        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(endDate)!!

        for (i in 1..listTotal) {
            val weekInfo = WeekInfo()
            for (j in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                val dayInfo = buildDayInfo(calendar, bindCalendar, todayCalendar, 0)
                weekInfo.weekContent[j] = dayInfo
            }
            list.add(weekInfo)
        }
        return list
    }

    private fun getListStartDate(bindDate: String): String {
        //得到绑定日期是周几
        val bindDayWeekIndex = dateUtil.getWeekIndexByDate(
            dateUtil.parseDateTimeStrToDate(
                "$bindDate 00:00:00",
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            )
        )
        Log.d("xcl_debug", "getListStartDate: $bindDayWeekIndex")
        //拿到绑定日期这一周的起始日期 周日开始
        return dateUtil.getOffsetDateByDay(
            -bindDayWeekIndex,
            bindDate,
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        )
    }

    private fun getListEndDate(): String {
        //得到今天是周几
        val endDayWeekIndex = dateUtil.getWeekIndexByDate(
            dateUtil.parseDateTimeStrToDate(
                endDate,
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            )
        )
        //拿到今天这一周的结束日期
        return dateUtil.getOffsetDateByDay(
            6 - endDayWeekIndex,
            endDate,
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        )
    }

    fun buildInitDate(curDateType: Int): DayInfo {
        return DayInfo().apply {
            type = curDateType
            isSelected = true
            isToday =
                (dateUtil.getCurrentDateTimeStr(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) == endDate)
            year = endDate.split("-")[0].toInt()
            month = endDate.split("-")[1].toInt()
            day = endDate.split("-")[2].toInt()
        }
    }
}