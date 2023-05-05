package cc.fastcv.traceapp

import android.app.Application
import android.util.Log
import org.json.JSONObject

object SensorsDataAPI {

    internal const val SDK_VERSION = "1.0.0"

    private const val TAG = "SensorsDataAPI"

    private var mDeviceId:String? = null
    private var mDeviceInfo:Map<String, Any>? = null

    fun init(app:Application) {
        mDeviceId = getAndroidID(app.applicationContext)
        mDeviceInfo = getDeviceInfo(app.applicationContext)
        registerActivityLifecycleCallbacks(app)
    }

    /**
     * 指定不采集哪个 Activity 的页面浏览事件
     *
     * @param activity Activity
     */
    fun ignoreAutoTrackActivity(activity: Class<*>?) {
        ignoreAutoTrackActivityInner(activity)
    }

    /**
     * 恢复采集某个 Activity 的页面浏览事件
     *
     * @param activity Activity
     */
    fun removeIgnoredActivity(activity: Class<*>?) {
        removeIgnoredActivityInner(activity)
    }

    /**
     * track 事件
     *
     * @param eventName  String 事件名称
     * @param properties JSONObject 事件自定义属性
     */
    fun track(eventName: String, properties: JSONObject?) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("event", eventName)
            jsonObject.put("device_id", mDeviceId)
            val sendProperties = JSONObject(mDeviceInfo)
            if (properties != null) {
                mergeJSONObject(properties, sendProperties)
            }
            jsonObject.put("properties", sendProperties)
            jsonObject.put("time", System.currentTimeMillis())
            Log.i(TAG, formatJson(jsonObject.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}