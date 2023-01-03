package cc.fastcv.bluetoothdemo.lib

import android.util.Log
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Util {

    companion object {
        private val TAG: String = "Util"
        val EXECUTOR: Executor = Executors.newCachedThreadPool()

        fun mkdirs(filePath: String?) {
            val mk = File(filePath).mkdirs()
            Log.d(TAG, "mkdirs: $mk")
        }
    }
}