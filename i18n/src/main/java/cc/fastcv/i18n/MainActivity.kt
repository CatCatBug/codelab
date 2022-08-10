package cc.fastcv.i18n

import android.os.Bundle
import android.widget.ImageView

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        findViewById<ImageView>(R.id.iv_setting).also {
            it.setOnClickListener {
                I18nSettingActivity.intoActivity(this)
            }
        }
    }
}