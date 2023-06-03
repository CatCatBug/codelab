package com.umeox.skin_demo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.umeox.skin_lib.config.SkinConfig
import com.umeox.skin_lib.listener.ILoaderListener
import com.umeox.skin_lib.loader.SkinManager
import java.io.File
import java.io.FileOutputStream

object Utils {

    fun copyFile(context: Context, rawId: Int, targetFile: File) {
        val startTime = System.currentTimeMillis()
        val inputStream = context.resources.openRawResource(rawId)

        val outputStream = FileOutputStream(targetFile)
        val buffer = ByteArray(1024)
        var length = inputStream.read(buffer)
        while (length > 0) {
            outputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        outputStream.close()
        inputStream.close()
        Log.d("xcl_debug", "copyFile: 文件${targetFile.name}复制成功  用时：${System.currentTimeMillis() - startTime}ms")
    }


    fun applyNight(context: Context) {
//        val skin = File(getExternalFilesDir(null),"BlackFantacy.skin")
        if (!SkinConfig.useSkinPackage) {
            SkinManager.load("night",
                object : ILoaderListener {
                    override fun onStart() {
                        Log.e("xcl_debug", "startloadSkin")
                    }

                    override fun onSuccess() {
                        Log.e("xcl_debug", "loadSkinSuccess")
                    }

                    override fun onFailed() {
                        Log.e("xcl_debug", "loadSkinFail")
                    }
                })
            return
        }

        val skin = File(context.getExternalFilesDir(null),"t1.skin")
        if (!skin.exists()) {
            Log.e("xcl_debug", "文件不存在，尝试复制皮肤文件---")
            Utils.copyFile(context,R.raw.t1,skin)
            if (!skin.exists()) {
                Toast.makeText(
                    context,
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
                }

                override fun onFailed() {
                    Log.e("xcl_debug", "loadSkinFail")
                }
            })
    }

}