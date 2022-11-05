package cc.fastcv.jetpack.executor

import androidx.annotation.NonNull

abstract class CvTaskExecutor {
    abstract fun executeOnDiskIO(runnable: Runnable)

    abstract fun postToMainThread(runnable: Runnable)

    fun executeOnMainThread(@NonNull runnable: Runnable) {
        if (isMainThread()) {
            runnable.run()
        } else {
            postToMainThread(runnable)
        }
    }

    abstract fun isMainThread() : Boolean
}