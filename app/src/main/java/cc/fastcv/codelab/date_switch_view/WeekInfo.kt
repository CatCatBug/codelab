package cc.fastcv.codelab.date_switch_view

import java.util.*

class WeekInfo {
    var weekContent = Array(7) {
        DayInfo()
    }
}

class DayInfo {
    var type = 0
    var isSelected = false
    var isToday = false
    var isInvalid = false
    var year = 0
    var month = 0
    var day = 0
    override fun toString(): String {
        return "DayInfo(type=$type, isSelected=$isSelected, isToday=$isToday, isInvalid=$isInvalid, year=$year, month=$month, day=$day)"
    }

    fun getDateYmd(): String {
        return "$year-${String.format(Locale.ENGLISH,"%02d", month)}-${String.format(Locale.ENGLISH,"%02d", day)}"
    }

}
