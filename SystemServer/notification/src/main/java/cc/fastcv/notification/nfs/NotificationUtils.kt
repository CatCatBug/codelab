package cc.fastcv.notification.nfs

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

/**
 * 判断通知监听服务是否开启
 */
fun getNotificationListenerServerStatus(app: Application): Boolean {
    val pkgName: String = app.packageName
    val flat: String = Settings.Secure.getString(
        app.contentResolver,
        ENABLED_NOTIFICATION_LISTENERS
    )
    if (!TextUtils.isEmpty(flat)) {
        val names = flat.split(":").toTypedArray()
        for (i in names.indices) {
            val cn = ComponentName.unflattenFromString(names[i])
            if (cn != null) {
                if (TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
    }
    return false
}

/**
 * 跳转去开启通知监听服务
 */
fun openNotificationAccess(context: Context) {
    context.startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
}

/**
 * 获取免打扰dnd模式开关状态
 */
fun getZenModeStatus(app: Application): Boolean {
    return Settings.Global.getInt(app.contentResolver, "zen_mode") != 0
}

/**
 * 判断通知总开关是否开启
 */
fun isNotificationEnabled(context: Context): Boolean {
    var isOpened = false
    isOpened = try {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
    return isOpened
}

/**
 * 安卓8.0及以上判断通知渠道是否开启了通知权限
 */
@RequiresApi(Build.VERSION_CODES.O)
fun isNotificationEnabled(context: Context, channel: NotificationChannel): Boolean {
    return channel.importance != NotificationManager.IMPORTANCE_NONE
}

/**
 * 进入通知权限设置界面
 */
fun gotoNotificationSettings(context: Context) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= 26) {
        // android 8.0引导
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
    } else {
        // android 5.0-7.0
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
    }
    intent.putExtra("app_package", context.packageName)
    intent.putExtra("app_uid", context.applicationInfo.uid)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}