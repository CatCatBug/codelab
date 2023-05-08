package com.fastcv.layerlist.animatedRotate

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.fastcv.layerlist.R

class AnimatedRotateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_animated_rotate)

        val icAnim = findViewById<ImageView>(R.id.ic_anim)
        Log.d("xcl_debug", "onCreate: ${icAnim.drawable}")




    }

}