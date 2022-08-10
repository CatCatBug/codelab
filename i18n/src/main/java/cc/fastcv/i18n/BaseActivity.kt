package cc.fastcv.i18n

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        //配置多语言
        super.attachBaseContext(I18nManager.applyAppLanguage(newBase))
    }

}