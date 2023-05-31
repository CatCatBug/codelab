package com.umeox.logger

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.MainThread

/**
 * 全局使用的日志类
 */
object Logger {

    //是否已经初始化
    private var initialized = false

    private var interceptFirst: AbsLogInterceptChain? = null

    private var logAble = false

    /**
     * 设置日志打印的开关
     */
    fun logAble(enable: Boolean) {
        logAble = enable
    }

    /**
     * 设置是否记录到日志文件的开关
     */
    fun logSaveAble(enable: Boolean) {
        LogFileManager.logSaveAble(enable)
    }

    fun i(tag: String, message: String) {
        log(Log.INFO, message, tag)
    }

    fun d(tag: String, message: String) {
        log(Log.DEBUG, message, tag)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(Log.ERROR, message, tag, throwable = throwable)
    }

    fun w(tag: String, message: String) {
        log(Log.WARN, message, tag)
    }

    @MainThread
    fun initLogger(context: Application) {
        //初始化操作
        if (initialized) {
            LogFileManager.setContext(context)
            initialized = true
            return
        }

        interceptFirst = LoggerDecorateInterceptor().apply {
            addIntercept(LoggerPrintInterceptor().apply {
                addIntercept(Logger2FileInterceptor())
            })
        }
    }

    @Synchronized
    private fun log(
        priority: Int,
        message: String,
        tag: String,
        throwable: Throwable? = null
    ) {
        if (logAble) {
            interceptFirst?.intercept(priority, tag, message, throwable)
        }
    }
}