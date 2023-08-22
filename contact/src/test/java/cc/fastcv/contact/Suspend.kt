package cc.fastcv.contact

import kotlin.coroutines.*

class Suspend(block: suspend Scope.() -> Unit) : Scope {

    private var continuation: Continuation<Unit>? = null

    private var num: Int = 0

    init {
        val coroutineBlock: suspend Scope.() -> Unit =
            { block() }
        coroutineBlock.startCoroutine(this, object : Continuation<Unit> {
            override fun resumeWith(result: Result<Unit>) {
            }

            override val context = EmptyCoroutineContext
        })
    }

    fun c(): Int {
        val result = num
        println("获取结果并恢复携程$result")
        continuation?.resume(Unit)
        return result
    }

    override suspend fun g(value: Int) {
        return suspendCoroutine { continuation ->
            this.continuation = continuation
            this.num = value
            println("设置只与携程对象$value")
        }
    }
}