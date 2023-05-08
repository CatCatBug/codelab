package cc.fastcv.i18n

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class I18nSettingActivity : BaseActivity(), ItemClickListener {

    companion object {
        fun intoActivity(context: Context) {
            context.startActivity(Intent(context,I18nSettingActivity::class.java))
        }
    }

    private lateinit var adapter: LanguageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_i18n_setting)

        initView()
    }

    private fun initView() {
        findViewById<ImageView>(R.id.iv_back).also {
            it.setOnClickListener {
                finish()
            }
        }

        findViewById<ImageView>(R.id.iv_save).also {
            it.setOnClickListener {
                if (I18nManager.updateLanguage(adapter.getSelectIndex())) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    finish()
                }
            }
        }


        findViewById<RecyclerView>(R.id.rv_language).also {
            adapter = LanguageListAdapter(I18nManager.getSupportLanguageList())
            adapter.setOnItemClickListener(this)
            it.adapter = adapter
        }
    }

    override fun onItemClickListener(data: LanguageItem, position: Int) {
        adapter.chooseLanguageItem(data)
    }
}