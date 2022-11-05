package cc.fastcv.jetpack.lifecycle.ext

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import cc.fastcv.jetpack.lifecycle.CvLifecycle
import cc.fastcv.jetpack.lifecycle.CvLifecycleOwner
import cc.fastcv.jetpack.lifecycle.CvLifecycleRegistry
import cc.fastcv.jetpack.lifecycle.CvReportFragment

class CvProcessLifecycleOwner private constructor() : CvLifecycleOwner {

    companion object {
        const val TIMEOUT_MS = 700L

        private val sInstance = CvProcessLifecycleOwner()

        fun get(): CvLifecycleOwner {
            return sInstance
        }

        fun init(context: Context) {
            sInstance.attach(context)
        }
    }

    private var mStartedCounter = 0

    private var mResumedCounter = 0

    private var mPauseSent = true

    private var mStopSent = true

    private lateinit var mHandler: Handler

    private val mRegistry = CvLifecycleRegistry(this)

    private val mDelayedPauseRunnable = Runnable {
        dispatchPauseIfNeeded()
        dispatchStopIfNeeded()
    }

    private val mInitializationListener = object : CvReportFragment.ActivityInitializationListener {
        override fun onCreate() {}

        override fun onStart() {
            activityStarted()
        }

        override fun onResume() {
            activityResumed()
        }
    }


    fun activityStarted() {
        mStartedCounter++
        if (mStartedCounter == 1 && mStopSent) {
            mRegistry.handleLifecycleEvent(CvLifecycle.Event.ON_START)
            mStopSent = false
        }
    }

    fun activityResumed() {
        mResumedCounter++
        if (mResumedCounter == 1) {
            if (mPauseSent) {
                mRegistry.handleLifecycleEvent(CvLifecycle.Event.ON_RESUME)
                mPauseSent = false
            } else {
                mHandler.removeCallbacks(mDelayedPauseRunnable)
            }
        }
    }

    fun activityPaused() {
        mResumedCounter--
        if (mResumedCounter == 0) {
            mHandler.postDelayed(mDelayedPauseRunnable, TIMEOUT_MS)
        }
    }

    fun activityStopped() {
        mStartedCounter--
        dispatchStopIfNeeded()
    }

    private fun dispatchPauseIfNeeded() {
        if (mResumedCounter == 0) {
            mPauseSent = true
            mRegistry.handleLifecycleEvent(CvLifecycle.Event.ON_PAUSE)
        }
    }

    private fun dispatchStopIfNeeded() {
        if (mStartedCounter == 0 && mPauseSent) {
            mRegistry.handleLifecycleEvent(CvLifecycle.Event.ON_STOP)
            mStopSent = true
        }
    }

    fun attach(context: Context) {
        mHandler = Handler(Looper.getMainLooper())
        mRegistry.handleLifecycleEvent(CvLifecycle.Event.ON_CREATE)
        val app = context.applicationContext as Application
        app.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.registerActivityLifecycleCallbacks(object :
                    EmptyActivityLifecycleCallbacks() {

                    override fun onActivityPostStarted(activity: Activity) {
                        activityStarted()
                    }

                    override fun onActivityPostResumed(activity: Activity) {
                        activityResumed()
                    }

                })
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (Build.VERSION.SDK_INT < 29) {
                    CvReportFragment.get(activity).setProcessListener(mInitializationListener)
                }
            }

            override fun onActivityPaused(activity: Activity) {
                activityPaused()
            }

            override fun onActivityStopped(activity: Activity) {
                activityStopped()
            }

        })
    }


    override fun getLifecycle(): CvLifecycle {
        return mRegistry
    }
}