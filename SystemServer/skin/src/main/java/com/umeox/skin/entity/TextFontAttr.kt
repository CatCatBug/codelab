package com.umeox.skin.entity

import android.view.View
import android.widget.TextView

class TextFontAttr : Attr() {

    override fun apply(view: View) {}

    override fun applyTextFont(view: View, replaceTable: Map<String, String>) {
        if ("font" == attrValueTypeName) {
            com.umeox.skin.SkinManager.log("apply fontFamily")
            if (view is TextView) {
                view.typeface = getTextFont()
            }
        }
    }
}