package com.umeox.skin_demo

import android.content.Context
import android.util.Log
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

}