package cc.fastcv.sqlite

import android.app.Application
import cc.fastcv.sqlite.db.Database

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }

}