package cc.fastcv.sqlite

import android.app.Application
import android.util.Log
import cc.fastcv.sqlite.db.Database
import cc.fastcv.sqlite.util.isMainProcess


class App : Application() {

    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        if (isMainProcess(this)) {
            Log.d(TAG, "onCreate: 主线程启动")
            Database.init(this)
        }
    }
}