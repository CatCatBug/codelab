package cc.fastcv.notification

import android.app.Notification
import android.app.Notification.BADGE_ICON_LARGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.animation.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import cc.fastcv.notification.nfs.NotificationListener
import cc.fastcv.notification.nfs.openNotificationAccess


class MainActivity : AppCompatActivity() {

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btOpen).setOnClickListener {
            openNotificationAccess(this)
        }
        findViewById<Button>(R.id.btClose).setOnClickListener {
            stopService(Intent(this, NotificationListener::class.java))
        }
        findViewById<Button>(R.id.btSendNotification1).setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                sendNotification1()
            }, 5000)

        }
        findViewById<Button>(R.id.btSendNotification2).setOnClickListener {
            val zenMode = Settings.Global.getInt(contentResolver, "zen_mode")
            Log.d("xcl_debug", "zen_mode: $zenMode")

        }
    }


    private fun sendNotification1() {
        // 注意：PendingIntent.FLAG_UPDATE_CURRENT
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("key", "value")
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this, 2000, intent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, 2000, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 创建通知(标题、内容、图标)
            val notification: Notification = Notification.Builder(this)
                .setContentTitle("通知标题")
                .setContentText("通知内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

            // 创建通知管理器
            // 发送通知
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(1, notification)
        } else {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            // 1. 创建一个通知(必须设置channelId)
            val channelId = "ChannelId"
            val notification = Notification.Builder(applicationContext, channelId)
                .setContentTitle("普通通知") // 标题
                .setStyle(Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, R.mipmap.ttt)))
                .setSmallIcon(R.drawable.ic_baseline_perm_phone_msg_24) // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ttt)) // 大图标
                .setPriority(Notification.PRIORITY_DEFAULT) // 7.0 设置优先级
                .setContentIntent(pendingIntent) // 跳转配置
                .setNumber(10)
                .setBadgeIconType(BADGE_ICON_LARGE)
                .setProgress(100, 10, false)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
                .setVisibility(Notification.VISIBILITY_PRIVATE) // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
                .setAutoCancel(true)  // 是否自动消失（点击）or mManager.cancel(mNormalNotificationId)、cancelAll、setTimeoutAfter()

            // 2. 获取系统的通知管理器(必须设置channelId)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                "通知的渠道名称",
                NotificationManager.IMPORTANCE_HIGH
            )
            //创建通道
            notificationManager.createNotificationChannel(channel)
            //删除通道
//            notificationManager.deleteNotificationChannel(channelId)
            // 3. 发送通知(Notification与NotificationManager的channelId必须对应)
            notificationManager.notify(id++, notification.build())
        }

    }

}