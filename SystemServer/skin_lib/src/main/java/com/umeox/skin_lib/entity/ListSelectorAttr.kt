package com.umeox.skin_lib.entity

import android.view.View
import android.widget.AbsListView
import com.umeox.skin_lib.SkinManager

class ListSelectorAttr : Attr() {

    override fun apply(view: View) {
        if (view is AbsListView) {
            if ("color" == attrValueTypeName) {
                SkinManager.log("apply ListSelector Color")
                view.setSelector(getColor())
            }

            if ("drawable" == attrValueTypeName) {
                SkinManager.log("apply ListSelector Drawable")
                view.selector = getDrawable()
            }
        }
    }
}