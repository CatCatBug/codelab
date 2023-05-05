package cc.fastcv.notification.nfs

import android.app.Service
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    companion object {
        private const val TAG = "NotificationListener"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"notification service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"NotificationListener notification service重启模式:$flags")
        return Service.START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        Log.d(TAG,"消息监听服务notification service onDestroy")
        super.onDestroy()
    }

    override fun onListenerConnected() {
        Log.d(TAG,"notification service onListenerConnected")
    }

    override fun onNotificationRankingUpdate(rankingMap: RankingMap?) {
        Log.d(TAG,"notification service onNotificationRankingUpdate rankingMap = $rankingMap")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        Log.d(TAG,"notification service onNotificationPosted")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        Log.d(TAG,"notification service onNotificationRemoved")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG,"notification service onListenerDisconnected")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(TAG, "onTaskRemoved: ")
        stopSelf()
    }
}