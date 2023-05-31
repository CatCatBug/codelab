package com.umeox.logger

class LoggerDecorateInterceptor: AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String,throwable: Throwable?) {
        val decorateMsg =  "${Thread.currentThread().name} ---> $logMsg"
        super.intercept(priority, tag, decorateMsg, throwable)
    }

}