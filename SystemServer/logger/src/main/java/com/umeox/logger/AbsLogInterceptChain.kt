package com.umeox.logger

/**
 * 抽线的日志拦截处理类
 */
abstract class AbsLogInterceptChain {

    private var next: AbsLogInterceptChain? = null

    fun addIntercept(interceptChain: AbsLogInterceptChain?) {
        next = interceptChain
    }

    open fun intercept(priority: Int, tag: String, logMsg: String,throwable: Throwable? = null) {
        next?.intercept(priority, tag, logMsg,throwable)
    }
}