package com.umeox.skin_lib.entity

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.ListView
import com.umeox.skin_lib.loader.SkinManager

class DividerAttr : SkinAttr() {

    var dividerHeight = 1

    override fun apply(view: View) {
        if (view is ListView) {
            if (RES_TYPE_NAME_COLOR == attrValueTypeName) {
                Log.i("xcl_debug", "apply Divider Color")
                val color: Int = SkinManager.getColor(attrValueRefId)
                val sage = ColorDrawable(color)
                view.divider = sage
                view.dividerHeight = dividerHeight
            } else if (RES_TYPE_NAME_DRAWABLE == attrValueTypeName) {
                Log.i("xcl_debug", "apply Divider Drawable")
                view.divider = SkinManager.getDrawable(attrValueRefId)
            }
        }
    }
}