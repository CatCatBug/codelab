package com.umeox.skin_demo

import android.os.Bundle
import com.umeox.skin_demo.databinding.ActivityMvvmBinding
import com.umeox.skin_lib.base.SkinActivity

class MvvmActivity : SkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mvvmBinding = ActivityMvvmBinding.inflate(layoutInflater)
        setContentView(mvvmBinding.root)
    }

}