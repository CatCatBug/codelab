package com.umeox.skin_lib.entity

import android.view.View
import android.widget.TextView
import com.umeox.skin_lib.SkinManager

class TextColorAttr : Attr() {

    override fun apply(view: View) {
        if (view is TextView) {
            if ("color" == attrValueTypeName) {
                SkinManager.log("apply TextColor")
                view.setTextColor(getColorStateList())
            }
        }
    }

}