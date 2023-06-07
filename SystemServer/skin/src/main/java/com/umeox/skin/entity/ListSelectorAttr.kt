package com.umeox.skin.entity

import android.view.View
import android.widget.AbsListView

class ListSelectorAttr : Attr() {

    override fun apply(view: View) {
        if (view is AbsListView) {
            if ("color" == attrValueTypeName) {
                com.umeox.skin.SkinManager.log("apply ListSelector Color")
                view.setSelector(getColor())
            }

            if ("drawable" == attrValueTypeName) {
                com.umeox.skin.SkinManager.log("apply ListSelector Drawable")
                view.selector = getDrawable()
            }
        }
    }
}