package com.umeox.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            NotificationUtil.createNotificationForNormal(it)
            Log.d(
                "xcl_debug", "任务结束 目标时间： ${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(
                        Date(System.currentTimeMillis())
                    )
                }"
            )
            val file = File(context.externalCacheDir, "log.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.appendText(
                "任务结束 目标时间： ${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(
                        Date(System.currentTimeMillis())
                    )
                }\n"
            )
        }
    }
}