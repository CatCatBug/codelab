package cc.fastcv.i18n

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log

class App : Application(), Application.ActivityLifecycleCallbacks {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    private var activityStatus = false

    override fun onCreate() {
        super.onCreate()
        AppManager.initAppManager(this)
        I18nManager.initI18n(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        //APP退到后台或进入任务列表
        activityStatus = true;
        Log.d("xcl_debug", "onTrimMemory: APP退到后台或者进入任务列表")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        activityStatus = false;
        Log.d("xcl_debug", "onActivityResumed: APP又获取了焦点")
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        //APP进入任务列表且被杀死
        if (activityStatus) {
            Log.d("xcl_debug", "onActivityResumed: app 被杀死")
        }
        unregisterActivityLifecycleCallbacks(this)
    }


}