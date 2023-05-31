package com.umeox.logger

import android.util.Log

class LoggerPrintInterceptor: AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String,throwable: Throwable?) {
        when(priority) {
            Log.INFO -> {
                Log.i(tag, logMsg)
            }
            Log.WARN -> {
                Log.w(tag, logMsg)
            }
            Log.DEBUG -> {
                Log.d(tag, logMsg)
            }
            Log.ERROR -> {
                Log.e(tag, logMsg,throwable)
            }
        }
    }

}