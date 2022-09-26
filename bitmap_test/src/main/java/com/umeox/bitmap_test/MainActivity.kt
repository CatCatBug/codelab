package com.umeox.bitmap_test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bkg = BitmapFactory.decodeResource(
            resources,
            R.mipmap.login_background_img
        )
        Log.d("xcl_debug", "bkg.width: ${bkg.width}    bkg.height: ${bkg.height} ")
        Log.d("xcl_debug", "bit size: ${bkg.allocationByteCount} ")
        Log.d("xcl_debug", "total size: ${
            Formatter.formatFileSize(this,
                bkg.allocationByteCount.toLong()
        )}")
    }
}