package com.fastcv.layerlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fastcv.layerlist.animatedRotate.AnimatedRotateActivity
import com.fastcv.layerlist.clip.ClipActivity
import com.fastcv.layerlist.inset.InsetActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBtAnimatedRotate()
        initBtClip()
        initBtInset()
    }

    private fun initBtInset() {
        val btInset = findViewById<Button>(R.id.btInset)
        btInset.setOnClickListener {
            startActivity(Intent(this,InsetActivity::class.java))
        }
    }

    private fun initBtClip() {
        val btClip = findViewById<Button>(R.id.btClip)
        btClip.setOnClickListener {
            startActivity(Intent(this,ClipActivity::class.java))
        }
    }

    private fun initBtAnimatedRotate() {
        val btAnimatedRotate = findViewById<Button>(R.id.btAnimatedRotate)
        btAnimatedRotate.setOnClickListener {
            startActivity(Intent(this,AnimatedRotateActivity::class.java))
        }
    }
}