package cc.fastcv.ble.sdk

import android.app.Application

object SDKManager {

    private lateinit var app: Application

    fun init(app:Application) {
        this.app = app
    }

    fun getApp() = app

}