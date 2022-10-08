package com.umeox.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    val coroutineContext = EmptyCoroutineContext + CoroutineExceptionHandler {
        Log.d(TAG, "获取异常: ${it.message}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val continuation = suspend {
//            Log.d(TAG, "In Coroutine")
//            5
//        }.createCoroutine(object : Continuation<Int> {
//            override val context: CoroutineContext = EmptyCoroutineContext
//
//            override fun resumeWith(result: Result<Int>) {
//                Log.d(TAG, "Coroutine End:$result")
//            }
//        })
//        continuation.resume(Unit)

        callLaunchCoroutine()
//        lifecycleScope.launch {
//            try {
//                val suspendFun2 = suspendFun2("a", "b")
//                Log.d(TAG, "获取到结果: $suspendFun2")
//            } catch (e:Exception) {
//                e.printStackTrace()
//            }
//        }









//        lifecycleScope.launch(Dispatchers.IO) {
//            val deffer1 = async {
//                getArraySum1()
//            }
//
//            val deffer2 = async {
//                getArraySum2()
//            }
//            var sum1 = deffer1.await()
//            var sum2 = deffer2.await()
//            Log.d(TAG, "onCreate: 获取结果：${sum1 + sum2}")
//        }
    }

    private suspend fun suspendFun2(a :String, b :String) = suspendCoroutine<Int> {
        Thread {
            try {
                Log.d(TAG, "suspendFun2: a:$a   b:$b")
                Thread.sleep(3000)
                throw NullPointerException()
                it.resumeWith(Result.success(1000))
            } catch (e:Exception) {
                it.resumeWith(Result.failure(e))
            }
        }.start()
    }

    private fun callLaunchCoroutine() {
        launchCoroutine(ProducerScope<Int>()) {
            Log.d(TAG, "In Coroutine")
            produce(1024)
            Log.d(TAG, "In Coroutine1")
            Log.d(TAG, "In Coroutine2")
            throw NullPointerException()
            produce(2048)
        }
    }

    private fun <R, T> launchCoroutine(receiver:R, block: suspend R.() -> T) {
        block.startCoroutine(receiver,object : Continuation<T> {
            override val context: CoroutineContext = coroutineContext

            override fun resumeWith(result: Result<T>) {
                Log.d(TAG, "Coroutine End:$result")
                result.onFailure {
                    context[CoroutineExceptionHandler]?.onError(it)
                }
            }
        })
    }

    class CoroutineExceptionHandler(val onErrorAction: (Throwable) -> Unit)
        : AbstractCoroutineContextElement(Key) {
        companion object Key: CoroutineContext.Key<CoroutineExceptionHandler>

        fun onError(error: Throwable) {
            error.printStackTrace()
            onErrorAction(error)
        }
    }
}

class ProducerScope<T> {
    suspend fun produce(value: T) : T {
        Log.d("MainActivity", "Coroutine End:$value")
        delay(1000)
        return value
    }
}