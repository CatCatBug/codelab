package com.umeox.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AlarmTaskManager {
    private val times = arrayListOf(8, 12, 15, 18, 20)

    fun startTask(context: Context, time: Long) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val calendar = Calendar.getInstance()

//        val alarmIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            Intent(context, AlarmReceiver::class.java),
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )

        val file = File(context.externalCacheDir, "log.txt")
        if (!file.exists()) {
            file.createNewFile()
        }

//        for (i in 0..6) {
//            calendar.add(Calendar.DAY_OF_MONTH, 0)
//            for (time in times) {
//                calendar[Calendar.HOUR_OF_DAY] = 10
//                calendar[Calendar.MINUTE] = 0
//                if (calendar.timeInMillis > System.currentTimeMillis()) {
        startTask(
            alarmMgr, System.currentTimeMillis() + time, PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, AlarmReceiver::class.java).apply {
                    addCategory("$time")
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ), file
        )
//                }
//            }
//        }
    }

    private fun startTask(
        alarmMgr: AlarmManager,
        timeInMillis: Long,
        intent: PendingIntent,
        file: File
    ) {
        Log.d(
            "xcl_debug",
            "开始任务 目标时间： ${
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(
                    Date(timeInMillis)
                )
            }"
        )
        file.appendText(
            "开始任务 目标时间： ${
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.ENGLISH
                ).format(Date(timeInMillis))
            }\n"
        )
        alarmMgr.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            intent
        )
    }

}