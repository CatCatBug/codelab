package com.umeox.logger

import android.app.Application
import android.os.Handler
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日志文件管理器
 */
internal object LogFileManager {

    //一天对应的毫秒数
    private const val DAY_MILLIS = 1000 * 60 * 60 * 24
    //日志保留的天数
    private const val SAVE_DAY_LIMIT = 3

    //指代日志文件
    private var file: File? = null

    //保存日志的线程
    private val fileSaveHandlerThread = LogSaveHandlerThread()
    private val handler = Handler(fileSaveHandlerThread.looper)

    //是否记录日志到文件中 默认是关闭的
    private var logSaveAble = false

    private var context: Application? = null

    fun setContext(context: Application) {
        this.context = context
    }

    /**
     * 设置日志写入文件开关
     */
    fun logSaveAble(enable: Boolean) {
        if (logSaveAble != enable) {
            if (enable) {
                fileSaveHandlerThread.start()
            } else {
                fileSaveHandlerThread.quitSafely()
            }
            logSaveAble = enable
        }
    }

    /**
     * 保存日志
     */
    fun saveIfNeed(priority: Int, tag: String, logMsg: String, throwable: Throwable?) {
        if (logSaveAble) {
            handler.post {
                if (file == null) {
                    createFile()
                    delFileIfNeed()
                }

                if (file != null) {
                    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
                        .toString()
                    val needWriteMessage = "$date ${getPriority(priority)}/$tag:$logMsg\n"
                    try {
                        file!!.appendText(needWriteMessage)
                        throwable?.printStackTrace(PrintWriter(file!!))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("LogFileManager", "saveIfNeed: ${e.message}")
                    }
                }
            }

        }
    }

    /**
     * 获取日志级别的字符
     */
    private fun getPriority(priority: Int): String {
        return when (priority) {
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.DEBUG -> "D"
            Log.ERROR -> "E"
            else -> "I"
        }
    }

    /**
     * 删除历史日志文件
     */
    private fun delFileIfNeed() {
        val files = context?.externalCacheDir?.listFiles()
        files?.forEach { it ->
            try {
                val old = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.CHINA
                ).parse(it.name.replace(".txt", "")) ?: return

                val timeInMillis = Calendar.getInstance().apply {
                    time = old
                }.timeInMillis

                val dDay = (System.currentTimeMillis() - timeInMillis) / DAY_MILLIS
                if (dDay >= SAVE_DAY_LIMIT) {
                    it.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("LogFileManager", "delFileIfNeed: ${e.message}")
            }
        }
    }

    /**
     * 创建日志文件
     */
    private fun createFile() {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date())
        val cacheDir = context?.externalCacheDir
        cacheDir?.let {
            //Log.i("创建文件","创建文件");
            file = File(it, "$format.txt")
            if (!file!!.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    file!!.createNewFile()
                } catch (e: Exception) {
                    Log.d("Log", "init: ${e.message}")
                    return
                }
            }
        }
    }
}