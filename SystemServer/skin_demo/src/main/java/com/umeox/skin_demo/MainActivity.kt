package com.umeox.skin_demo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.umeox.skin_lib.base.SkinActivity
import com.umeox.skin_lib.config.SkinConfig
import com.umeox.skin_lib.listener.ILoaderListener
import com.umeox.skin_lib.loader.SkinManager
import java.io.File

class MainActivity : SkinActivity() {

    private val SKIN_NAME = "BlackFantacy.skin"

    private var isOfficalSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isOfficalSelected = SkinConfig.isDefaultSkin()

        if (!isOfficalSelected) {
            findViewById<SwitchCompat>(R.id.sc_theme).isChecked = true
        }

        findViewById<SwitchCompat>(R.id.sc_theme).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSkinSetClick()
            } else {
                onSkinResetClick()
            }
        }
    }

    private fun onSkinResetClick() {
        if (!isOfficalSelected) {
            SkinManager.restoreDefaultTheme()
            Toast.makeText(applicationContext, "切换成功", Toast.LENGTH_SHORT).show()
            isOfficalSelected = true
        }
    }

    private fun onSkinSetClick() {
        if (!isOfficalSelected) return
        val skin = File(getExternalFilesDir(null),SKIN_NAME)
        if (!skin.exists()) {
            Log.e("xcl_debug", "文件不存在，尝试复制皮肤文件---")
            Utils.copyFile(this,R.raw.black_night,skin)
            if (!skin.exists()) {
                Toast.makeText(
                    applicationContext,
                    "请检查" + skin.name + "是否存在",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            Log.e("xcl_debug", "复制皮肤文件成功---")
        }
        SkinManager.load(skin.absolutePath,
            object : ILoaderListener {
                override fun onStart() {
                    Log.e("xcl_debug", "startloadSkin")
                }

                override fun onSuccess() {
                    Log.e("xcl_debug", "loadSkinSuccess")
                    Toast.makeText(applicationContext, "切换成功", Toast.LENGTH_SHORT).show()
                    isOfficalSelected = false
                }

                override fun onFailed() {
                    Log.e("xcl_debug", "loadSkinFail")
                    Toast.makeText(applicationContext, "切换失败", Toast.LENGTH_SHORT).show()
                }
            })
    }

}