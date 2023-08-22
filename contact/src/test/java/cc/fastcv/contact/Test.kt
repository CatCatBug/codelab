package cc.fastcv.contact

import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main() {
    val singleDispatcher = newSingleThreadContext("Single")

    runBlocking {
        val job = launch {
            with(Dispatchers.IO) {

            }

            with(Dispatchers.IO) {

            }
        }

        job.join()
    }
}