package com.umeox.skin_demo

import android.app.Application
import com.umeox.skin_lib.loader.SkinManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
        SkinManager.load()
    }

}