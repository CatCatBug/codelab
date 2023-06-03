package com.umeox.skin_lib.entity

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.umeox.skin_lib.loader.SkinManager

class BackgroundAttr : SkinAttr() {

    override fun apply(view: View) {
        if (RES_TYPE_NAME_COLOR == attrValueTypeName) {
            Log.i("xcl_debug", "apply background color     attrValueRefId = $attrValueRefId")
            view.setBackgroundColor(SkinManager.getColor(attrValueRefId))
        } else if (RES_TYPE_NAME_DRAWABLE == attrValueTypeName) {
            Log.i("xcl_debug", "apply background drawable     attrValueRefId = $attrValueRefId")
            val bg: Drawable? = SkinManager.getDrawable(attrValueRefId)
            view.background = bg
        }
    }

}