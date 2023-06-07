package com.umeox.skin_lib.entity

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.umeox.skin_lib.SkinManager

class BackgroundAttr : Attr() {

    override fun apply(view: View) {
        SkinManager.log("view = $view  attrValueTypeName = $attrValueTypeName")
        if ("color" == attrValueTypeName) {
            SkinManager.log("apply background color     attrValueRefId = $attrValueRefId")
            view.setBackgroundColor(getColor())
            return
        }

        if ("drawable" == attrValueTypeName) {
            SkinManager.log("apply background drawable     attrValueRefId = $attrValueRefId")
            val bg: Drawable? = getDrawable()
            view.background = bg
            return
        }
    }

}