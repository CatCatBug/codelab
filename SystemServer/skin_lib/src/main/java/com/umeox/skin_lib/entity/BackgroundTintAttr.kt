package com.umeox.skin_lib.entity

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import com.umeox.skin_lib.loader.SkinManager

class BackgroundTintAttr : SkinAttr() {
    override fun apply(view: View) {
        Log.i("xcl_debug", "apply backgroundTintList")
        view.backgroundTintList =
            ColorStateList.valueOf(SkinManager.getColor(attrValueRefId))
    }
}