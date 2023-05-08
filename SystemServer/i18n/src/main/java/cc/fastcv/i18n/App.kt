package cc.fastcv.i18n

import android.app.Application
import android.content.Context

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppManager.initAppManager(this)
        I18nManager.initI18n(this)
    }

}