package com.umeox.skin_demo

import android.app.Application
import com.umeox.skin_lib.SkinManager
import com.umeox.skin_lib.SkinMode

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this, SkinMode.INNER_SKIN_MODE)
    }

}