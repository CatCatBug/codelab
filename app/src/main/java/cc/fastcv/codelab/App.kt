package cc.fastcv.codelab

import android.app.Application
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("xcl_debug", "onCreate: ")
    }

}