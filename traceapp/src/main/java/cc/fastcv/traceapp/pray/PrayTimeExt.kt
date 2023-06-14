package cc.fastcv.traceapp.pray

class PrayTimeExt : PrayTime() {
    /**
     * 设置时间格式
     */
    fun setTimeFormat(is24HourFormat: Boolean) {
        timeFormat = if (is24HourFormat) {
            time24
        } else {
            Time12
        }
    }

    /**
     * 设置晡礼的算法，可选值为:
     *  0 Shafii; // Shafii (standard)
     *  1 Hanafi; // Hanafi
     */
    override fun setAsrJuristic(asrJuristic: Int) {
        if (asrJuristic != 0 || asrJuristic != 1) {
            throw IllegalArgumentException("asrJuristic value only is 0 or 1!!!")
        }
        super.setAsrJuristic(asrJuristic)
    }

    /**
     * 设置高纬度计算类型，可选值为：
     * 0 None; // No adjustment
     * 1 MidNight; // middle of night
     * 2 OneSeventh; // 1/7th of night
     * 3 AngleBased; // angle/60th of night
     */
    override fun setAdjustHighLats(adjustHighLats: Int) {
        if (adjustHighLats !in 0..3) {
            throw IllegalArgumentException("adjustHighLats value only in 0..3 !!!")
        }
        super.setAdjustHighLats(adjustHighLats)
    }

    /**
     * 时间偏移量（分钟）
     * 正数+  负数-
     * {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
     * ex: {0, 0, 0, 0, 0, 0, 0};
     */
    fun setTimeOffsets(offsetTimes: IntArray) {
        if (offsetTimes.size != 7) {
            throw IllegalArgumentException("offsetTimes array size must be 7 !!!")
        }
        tune(offsetTimes)
    }

    /**
     * 设置惯例参数
     */
    override fun setCustomParams(params: DoubleArray) {
        if (params.size != 5) {
            throw IllegalArgumentException("params array size must be 5 !!!")
        }
        super.setCustomParams(params)
    }
}