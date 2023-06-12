package com.umeox.workmanager

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object WorkManagerTaskManager {
    const val tag = "MainActivity"
    private val times = arrayListOf(8, 12, 15, 18, 20)

    fun startTask(context: Context, time: Long) {
//        val calendar = Calendar.getInstance()

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
        startTask(context, System.currentTimeMillis() + time, file)
//                }
//            }
//        }
    }

    private fun startTask(context: Context, timeInMillis: Long, file: File) {
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
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(
                    Date(timeInMillis)
                )
            }\n"
        )
        val uploadPicWork = OneTimeWorkRequestBuilder<UploadPicWork>()
            .addTag(tag)
            .setInitialDelay(timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(uploadPicWork)
    }

}