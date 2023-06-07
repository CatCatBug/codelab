package com.umeox.skin_lib.entity

import android.graphics.drawable.Drawable
import android.view.View
import com.umeox.skin_lib.SkinManager

class BackgroundAttr : Attr() {

    override fun apply(view: View) {
        if ("color" == attrValueTypeName) {
            SkinManager.log("BackgroundAttr apply background color - attrName = $attrName   attrValueRefName = $attrValueRefName")
            view.setBackgroundColor(getColor())
            return
        }

        if ("drawable" == attrValueTypeName) {
            SkinManager.log("BackgroundAttr apply background drawable  - attrName = $attrName   attrValueRefName = $attrValueRefName")
            val bg: Drawable? = getDrawable()
            view.background = bg
            return
        }
    }

}