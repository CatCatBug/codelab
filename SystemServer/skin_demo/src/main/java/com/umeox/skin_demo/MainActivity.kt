package com.umeox.skin_demo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.umeox.skin_lib.base.SkinActivity
import com.umeox.skin_lib.config.SkinConfig
import com.umeox.skin_lib.loader.SkinManager

class MainActivity : SkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!SkinConfig.isDefaultSkin()) {
            findViewById<SwitchCompat>(R.id.sc_theme).isChecked = true
        }

        findViewById<SwitchCompat>(R.id.sc_theme).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Utils.applyNight(this)
            } else {
                SkinManager.restoreDefaultTheme()
            }
        }

        findViewById<SwitchCompat>(R.id.sc_font).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SkinManager.applyTextFont(mapOf(
                    Pair("montserrat_italic","montserrat_black")
                ))
            } else {
                SkinManager.applyTextFont(mapOf(
                    Pair("montserrat_black","montserrat_italic")
                ))
            }
        }

        findViewById<TextView>(R.id.tv_native).setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }

        findViewById<TextView>(R.id.tv_mvvm).setOnClickListener {
            startActivity(Intent(this, MvvmActivity::class.java))
        }

        findViewById<TextView>(R.id.tv_fragment).setOnClickListener {
            startActivity(Intent(this, FragmentActivity::class.java))
        }
    }

}