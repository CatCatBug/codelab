package com.umeox.skin.entity

import android.view.View
import android.widget.TextView

class TextColorAttr : Attr() {

    override fun apply(view: View) {
        if (view is TextView) {
            if ("color" == attrValueTypeName) {
                com.umeox.skin.SkinManager.log("apply TextColor")
                view.setTextColor(getColorStateList())
            }
        }
    }

}