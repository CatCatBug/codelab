package com.umeox.logger

class Logger2FileInterceptor(): AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String,throwable: Throwable?) {
        LogFileManager.saveIfNeed(priority,tag,logMsg,throwable)
    }
}