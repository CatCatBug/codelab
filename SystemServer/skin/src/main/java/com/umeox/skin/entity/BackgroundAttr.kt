package com.umeox.skin.entity

import android.graphics.drawable.Drawable
import android.view.View

class BackgroundAttr : Attr() {

    override fun apply(view: View) {
        if ("color" == attrValueTypeName) {
            com.umeox.skin.SkinManager.log("apply background color     attrValueRefId = $attrValueRefId")
            view.setBackgroundColor(getColor())
        }

        if ("drawable" == attrValueTypeName) {
            com.umeox.skin.SkinManager.log("apply background drawable     attrValueRefId = $attrValueRefId")
            val bg: Drawable? = getDrawable()
            view.background = bg
        }
    }

}