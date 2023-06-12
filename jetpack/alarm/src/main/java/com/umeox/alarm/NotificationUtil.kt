package com.umeox.alarm

import android.app.Notification
import android.app.Notification.BADGE_ICON_LARGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

object NotificationUtil {

    fun createNotificationForNormal(context: Context) {
        // 注意：PendingIntent.FLAG_UPDATE_CURRENT
        val nm = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("key", "value")
        val pendingIntent =
            PendingIntent.getActivity(
                context, 2000, intent, PendingIntent.FLAG_IMMUTABLE
            )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 创建通知(标题、内容、图标)
            val notification: Notification = Notification.Builder(context)
                .setContentTitle("通知标题")
                .setContentText("通知内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

            // 创建通知管理器
            // 发送通知
            nm.notify(1, notification)
        } else {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            // 1. 创建一个通知(必须设置channelId)
            val channelId = "ChannelId";
            val notification = Notification.Builder(context, channelId)
                .setContentTitle("普通通知") // 标题
                .setContentText("普通通知内容") // 文本
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                ) // 大图标
                .setContentIntent(pendingIntent) // 跳转配置
                .setNumber(10)
                .setBadgeIconType(BADGE_ICON_LARGE)
                .addAction(R.mipmap.ic_launcher, "去看看", pendingIntent)// 通知上的操作
                .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
                .setVisibility(Notification.VISIBILITY_PRIVATE) // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
                .setAutoCancel(true)  // 是否自动消失（点击）or mManager.cancel(mNormalNotificationId)、cancelAll、setTimeoutAfter()

            val channel = NotificationChannel(
                channelId,
                "通知的渠道名称",
                NotificationManager.IMPORTANCE_HIGH
            )
            nm.createNotificationChannel(channel)
            // 3. 发送通知(Notification与NotificationManager的channelId必须对应)
            nm.notify(1, notification.build())
        }
    }

}