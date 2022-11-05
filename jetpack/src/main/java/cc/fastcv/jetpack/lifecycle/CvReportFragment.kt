package cc.fastcv.jetpack.lifecycle

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi

class CvReportFragment : Fragment() {

    companion object {
        private const val REPORT_FRAGMENT_TAG = ("com.fastcv"
                + ".LifecycleDispatcher.report_fragment_tag")

        fun injectIfNeededIn(activity: Activity) {
            Log.d("xcl_debug", "injectIfNeededIn: ")
            if (Build.VERSION.SDK_INT >= 29) {
                LifecycleCallbacks.registerIn(activity)
            }

            val manager = activity.fragmentManager
            if (manager.findFragmentByTag(REPORT_FRAGMENT_TAG) == null) {
                manager.beginTransaction().add(CvReportFragment(), REPORT_FRAGMENT_TAG).commit()
                manager.executePendingTransactions()
            }
        }

        fun dispatch(@NonNull activity: Activity, @NonNull event: CvLifecycle.Event) {
            if (activity is CvLifecycleRegistryOwner) {
                Log.d("xcl_debug", "is CvLifecycleRegistryOwner dispatch: ${event.name}")
                activity.getLifecycle().handleLifecycleEvent(event)
            }

            if (activity is CvLifecycleOwner) {
                Log.d("xcl_debug", "is CvLifecycleOwner dispatch: ${event.name}")
                val lifecycle = activity.getLifecycle()
                if (lifecycle is CvLifecycleRegistry) {
                    lifecycle.handleLifecycleEvent(event)
                }
            }
        }

        fun get(activity: Activity): CvReportFragment {
            return activity.fragmentManager.findFragmentByTag(REPORT_FRAGMENT_TAG) as CvReportFragment
        }
    }

    private var mProcessListener: ActivityInitializationListener? = null

    private fun dispatchCreate(listener: ActivityInitializationListener?) {
        listener?.onCreate()
    }

    private fun dispatchStart(listener: ActivityInitializationListener?) {
        listener?.onStart()
    }

    private fun dispatchResume(listener: ActivityInitializationListener?) {
        listener?.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dispatchCreate(mProcessListener)
        dispatch(CvLifecycle.Event.ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        dispatchStart(mProcessListener)
        dispatch(CvLifecycle.Event.ON_START)
    }

    override fun onResume() {
        super.onResume()
        dispatch(CvLifecycle.Event.ON_RESUME)
    }

    override fun onStop() {
        super.onStop()
        dispatch(CvLifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatch(CvLifecycle.Event.ON_DESTROY)
        mProcessListener = null
    }

    private fun dispatch(event: CvLifecycle.Event) {
        if (Build.VERSION.SDK_INT < 29) {
            dispatch(activity, event)
        }
    }

    fun setProcessListener(processListener: ActivityInitializationListener) {
        mProcessListener = processListener
    }

    interface ActivityInitializationListener {
        fun onCreate()

        fun onStart()

        fun onResume()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    class LifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        companion object {
            fun registerIn(activity: Activity) {
                activity.registerActivityLifecycleCallbacks(LifecycleCallbacks())
            }
        }

        override fun onActivityCreated(
            activity: Activity,
            bundle: Bundle?
        ) {
        }

        override fun onActivityPostCreated(
            activity: Activity,
            savedInstanceState: Bundle?
        ) {
            dispatch(activity, CvLifecycle.Event.ON_CREATE)
        }

        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityPostStarted(activity: Activity) {
            dispatch(activity, CvLifecycle.Event.ON_START)
        }

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPostResumed(activity: Activity) {
            dispatch(activity, CvLifecycle.Event.ON_RESUME)
        }

        override fun onActivityPrePaused(activity: Activity) {
            dispatch(activity, CvLifecycle.Event.ON_PAUSE)
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityPreStopped(activity: Activity) {
            dispatch(activity, CvLifecycle.Event.ON_STOP)
        }

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(
            activity: Activity,
            bundle: Bundle
        ) {
        }

        override fun onActivityPreDestroyed(activity: Activity) {
            dispatch(activity, CvLifecycle.Event.ON_DESTROY)
        }

        override fun onActivityDestroyed(activity: Activity) {}
    }
}