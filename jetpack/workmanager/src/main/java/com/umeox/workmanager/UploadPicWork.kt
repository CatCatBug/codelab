package com.umeox.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UploadPicWork(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        context.let {
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

        return Result.success()
    }
}