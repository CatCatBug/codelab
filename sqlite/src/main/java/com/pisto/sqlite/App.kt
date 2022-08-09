package com.pisto.sqlite

import android.app.Application
import com.pisto.sqlite.db.Database

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }

}