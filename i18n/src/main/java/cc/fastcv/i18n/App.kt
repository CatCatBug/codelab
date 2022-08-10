package cc.fastcv.i18n

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppManager.initAppManager(this)
        I18nManager.initI18n(this)
    }

}