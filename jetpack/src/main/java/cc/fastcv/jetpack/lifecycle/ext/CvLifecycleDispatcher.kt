package cc.fastcv.jetpack.lifecycle.ext

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import cc.fastcv.jetpack.lifecycle.CvReportFragment
import java.util.concurrent.atomic.AtomicBoolean

class CvLifecycleDispatcher private constructor() {

    companion object {
        private val sInitialized = AtomicBoolean(false)

        fun init(context: Context) {
            if (sInitialized.getAndSet(true)) {
                return;
            }
            (context.applicationContext as Application).registerActivityLifecycleCallbacks(
                DispatcherActivityCallback()
            )
        }
    }

    class DispatcherActivityCallback : EmptyActivityLifecycleCallbacks() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            CvReportFragment.injectIfNeededIn(activity)
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }
    }

}