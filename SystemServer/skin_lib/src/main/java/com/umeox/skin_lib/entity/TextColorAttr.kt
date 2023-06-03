package com.umeox.skin_lib.entity

import android.util.Log
import android.view.View
import android.widget.TextView
import com.umeox.skin_lib.loader.SkinManager

class TextColorAttr : SkinAttr() {
    override fun apply(view: View) {
        if (view is TextView) {
            if (RES_TYPE_NAME_COLOR == attrValueTypeName) {
                Log.i("xcl_debug", "apply TextColor")
                view.setTextColor(SkinManager.convertToColorStateList(attrValueRefId))
            }
        }
    }
}