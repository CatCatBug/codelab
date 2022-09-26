package com.umeox.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        lifecycleScope.launch(Dispatchers.IO) {
            val deffer1 = async {
                getArraySum1()
            }

            val deffer2 = async {
                getArraySum2()
            }
            var sum1 = deffer1.await()
            var sum2 = deffer2.await()
            Log.d(TAG, "onCreate: 获取结果：${sum1 + sum2}")
        }
    }

    private suspend fun getArraySum1() : Int {
        var sum = 0
        runBlocking {
            for (i in 1..100) {
                delay(100)
                sum += i
                Log.d(TAG, "getArraySum1: $sum    ${Thread.currentThread().name}")
            }
        }
        return sum
    }

    private suspend fun getArraySum2() : Int {
        var sum = 0
        runBlocking {
            for (i in 1..100) {
                delay(100)
                sum += i
                Log.d(TAG, "getArraySum2: $sum    ${Thread.currentThread().name}")
            }
        }
        return sum
    }
}