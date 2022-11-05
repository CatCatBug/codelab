package cc.fastcv.jetpack.executor

import androidx.annotation.NonNull

class CvArchTaskExecutor private constructor() : CvTaskExecutor() {

    companion object {
        @Volatile
        private var sInstance: CvArchTaskExecutor? = null

        fun getInstance(): CvArchTaskExecutor {
            if (sInstance != null) {
                return sInstance!!
            }
            synchronized(CvArchTaskExecutor::class.java) {
                if (sInstance == null) {
                    sInstance = CvArchTaskExecutor()
                }
            }
            return sInstance!!
        }

        private val sMainThreadExecutor = object : CvExecutor {
            override fun execute(command: Runnable) {
                getInstance().postToMainThread(command)
            }
        }

        private val sIOThreadExecutor = object : CvExecutor {
            override fun execute(command: Runnable) {
                getInstance().executeOnDiskIO(command)
            }
        }

        fun getMainThreadExecutor(): CvExecutor {
            return sMainThreadExecutor
        }

        fun getIOThreadExecutor(): CvExecutor {
            return sIOThreadExecutor
        }
    }

    private var mDelegate: CvTaskExecutor

    private var mDefaultTaskExecutor: CvTaskExecutor = CvDefaultTaskExecutor()

    init {
        mDelegate = mDefaultTaskExecutor
    }

    fun setDelegate(taskExecutor: CvTaskExecutor?) {
        mDelegate = taskExecutor ?: mDefaultTaskExecutor
    }

    override fun executeOnDiskIO(runnable: Runnable) {
        mDelegate.executeOnDiskIO(runnable)
    }

    override fun postToMainThread(runnable: Runnable) {
        mDelegate.postToMainThread(runnable)
    }

    override fun isMainThread(): Boolean {
        return mDelegate.isMainThread()
    }


}