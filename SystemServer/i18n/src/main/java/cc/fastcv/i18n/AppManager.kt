package cc.fastcv.i18n

import android.app.Application

object AppManager {

    private lateinit var application: Application

    fun initAppManager(application: Application) {
        this.application = application
    }

    fun getApplication() = application

}