package com.umeox.navigation.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umeox.navigation.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()

        Log.e("XXXXXXXX", "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.e("XXXXXXXX", "onStart")
    }
}
