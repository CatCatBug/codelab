package com.umeox.skin_demo

import android.os.Bundle
import com.umeox.skin_lib.base.SkinActivity

class FragmentActivity : SkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        initFragment()
    }

    private fun initFragment() {
        val fragment = DemoFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_area, fragment)
            .commit()
    }
}