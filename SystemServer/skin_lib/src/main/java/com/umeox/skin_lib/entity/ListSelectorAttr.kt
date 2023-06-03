package com.umeox.skin_lib.entity

import android.util.Log
import android.view.View
import android.widget.AbsListView
import com.umeox.skin_lib.loader.SkinManager

class ListSelectorAttr : SkinAttr() {
    override fun apply(view: View) {
        if (view is AbsListView) {
            if (RES_TYPE_NAME_COLOR == attrValueTypeName) {
                Log.i("xcl_debug", "apply ListSelector Color")
                view.setSelector(SkinManager.getColor(attrValueRefId))
            } else if (RES_TYPE_NAME_DRAWABLE == attrValueTypeName) {
                Log.i("xcl_debug", "apply ListSelector Drawable")
                view.selector = SkinManager.getDrawable(attrValueRefId)
            }
        }
    }
}