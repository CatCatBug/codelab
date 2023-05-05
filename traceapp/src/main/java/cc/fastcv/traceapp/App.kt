package cc.fastcv.traceapp

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SensorsDataAPI.init(this)
    }
}