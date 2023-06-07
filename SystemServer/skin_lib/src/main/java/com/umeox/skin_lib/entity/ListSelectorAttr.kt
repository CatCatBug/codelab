package com.umeox.skin_lib.entity

import android.view.View
import android.widget.AbsListView
import com.umeox.skin_lib.SkinManager

class ListSelectorAttr : Attr() {

    override fun apply(view: View) {
        if (view is AbsListView) {
            if ("color" == attrValueTypeName) {
                SkinManager.log("DividerAttr apply ListSelector color - attrName = $attrName   attrValueRefName = $attrValueRefName")
                view.setSelector(getColor())
            }

            if ("drawable" == attrValueTypeName) {
                SkinManager.log("DividerAttr apply ListSelector drawable - attrName = $attrName   attrValueRefName = $attrValueRefName")
                view.selector = getDrawable()
            }
        }
    }
}