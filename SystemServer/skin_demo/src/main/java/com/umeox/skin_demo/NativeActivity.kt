package com.umeox.skin_demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.umeox.skin_lib.ResourceManager
import com.umeox.skin_lib.SkinManager
import com.umeox.skin_lib.base.SkinActivity
import com.umeox.skin_lib.entity.DynamicAttr

class NativeActivity : SkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)



        findViewById<SwitchCompat>(R.id.sc_theme).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Utils.applyNight(this)
            } else {
                SkinManager.restoreDefaultTheme()
            }
        }

        findViewById<TextView>(R.id.tv_add).setOnClickListener {
            dynamicAddTitleView()
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<SwitchCompat>(R.id.sc_theme).isChecked = !SkinManager.isDefaultSkin()
    }

    @SuppressLint("SetTextI18n")
    private fun dynamicAddTitleView() {
        val textView = TextView(this)
        textView.text = "Small Article (动态new)"
        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = param
        textView.gravity = Gravity.CENTER
        textView.setPadding(0,20,0,20)
        textView.setTextColor(ResourceManager.getColor(R.color.color_text_1))
        textView.textSize = 20f

        findViewById<LinearLayout>(R.id.ll).addView(textView)
        val mDynamicAttr: MutableList<DynamicAttr> = mutableListOf()
        mDynamicAttr.add(DynamicAttr("textColor", R.color.color_text_1))
        dynamicAddView(textView, mDynamicAttr)
    }



}