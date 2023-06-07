package com.umeox.skin_lib.entity

import android.view.View
import android.widget.TextView
import com.umeox.skin_lib.SkinManager

class TextFontAttr : Attr() {

    override fun apply(view: View) {}

    override fun applyTextFont(view: View, replaceTable: Map<String, String>) {
        if ("font" == attrValueTypeName) {
            SkinManager.log("TextFontAttr apply fontFamily - attrName = $attrName   attrValueRefName = $attrValueRefName")
            if (view is TextView) {
                view.typeface = getTextFont()
            }
        }
    }
}