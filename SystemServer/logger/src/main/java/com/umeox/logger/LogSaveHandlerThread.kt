package com.umeox.logger

import android.os.HandlerThread
import android.util.Log

internal class LogSaveHandlerThread : HandlerThread("logger_save_thread") {

    override fun start() {
        super.start()
        Log.d("LogSaveHandlerThread", "start: ----->")
    }

    override fun quitSafely(): Boolean {
        Log.d("LogSaveHandlerThread", "quitSafely: ----->")
        return super.quitSafely()
    }


}