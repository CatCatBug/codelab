package com.umeox.livedata

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * APP管理类
 * 管理 activity 队列（最上层Activity）
 * 管理 应用的可见性（前台后台）
 */
object AppManager {

    /**
     * Activity列表管理器
     */
    private val activityListManager = ActivityListManager()

    /**
     * 应用可见性管理器
     */
    private val appVisibilityManager = AppVisibilityManager()

    /**
     * 全局监听Activity生命周期的回调
     */
    private val mCallbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            activityListManager.addActivity(activity)
            activityListManager.updateTopActivityWeakRef(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            activityListManager.updateTopActivityWeakRef(activity)
            appVisibilityManager.setAppVisibility(true)
        }

        override fun onActivityResumed(activity: Activity) {
            activityListManager.updateTopActivityWeakRef(activity)
        }

        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {
            appVisibilityManager.setAppVisibility(true)
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {
            activityListManager.removeActivity(activity)
        }
    }

    /**
     * 初始化管理器
     */
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(mCallbacks)
    }

    /**
     * 应用可见性管理类
     */
    class AppVisibilityManager {

        private var visibility = false

        /**
         * 设置应用可见性
         */
        fun setAppVisibility(visibility: Boolean) {
            this.visibility = visibility
        }

        /**
         * 返回应用可见性
         */
        fun getAppVisibility() = visibility

    }

    /**
     * Activity列表管理类
     */
    class ActivityListManager {

        /**
         * 最上层Activity的引用
         */
        private var topActivityWeakRef: WeakReference<Activity>? = null

        /**
         * Activity列表
         */
        private val activities = mutableListOf<Activity>()

        /**
         * 获取栈顶Activity
         * @return 栈顶Activity
         */
        fun getTopActivity(): Activity? {
            if (topActivityWeakRef != null) {
                val activity: Activity? = topActivityWeakRef!!.get()
                if (activity != null) {
                    return activity
                }
            }
            return null
        }

        /**
         * 判断Activity是否存在栈中
         *
         * @param activity activity
         * @return `true`: 存在<br></br>`false`: 不存在
         */
        fun isActivityExistsInStack(activity: Activity): Boolean {
            for (aActivity in activities) {
                if (aActivity == activity) {
                    return true
                }
            }
            return false
        }

        /**
         * 判断Activity是否存在栈中
         *
         * @param clz activity类
         * @return `true`: 存在<br></br>`false`: 不存在
         */
        fun isActivityExistsInStack(clz: Class<*>): Boolean {
            for (aActivity in activities) {
                if (aActivity.javaClass == clz) {
                    return true
                }
            }
            return false
        }

        /**
         * 更新最上层Activity对象引用
         */
        fun updateTopActivityWeakRef(activity: Activity) {
            if (topActivityWeakRef == null || activity != topActivityWeakRef!!.get()) {
                topActivityWeakRef = WeakReference(activity)
            }
        }

        /**
         * 新增Activity对象到列表中
         */
        fun addActivity(activity: Activity) {
            activities.add(activity)
        }

        /**
         * 从列表中移除Activity
         */
        fun removeActivity(activity: Activity) {
            activities.remove(activity)
        }

        fun getRoutePath(activity: Activity): String {
//            val annotations = activity::class.java.getAnnotation()
            return ""
        }
    }
}