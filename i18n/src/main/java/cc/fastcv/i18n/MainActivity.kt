package cc.fastcv.i18n

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

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


        findViewById<TextView>(R.id.tv_text).also {
            it.setOnClickListener {
                Toast.makeText(AppManager.getApplication(),R.string.i_love_you,Toast.LENGTH_SHORT).show()
            }
        }
    }
}