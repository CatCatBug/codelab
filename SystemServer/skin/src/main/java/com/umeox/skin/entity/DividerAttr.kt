package com.umeox.skin.entity

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ListView

class DividerAttr : Attr() {
    private var dividerHeight = 1

    override fun apply(view: View) {
        if (view is ListView) {
            if ("color" == attrValueTypeName) {
                com.umeox.skin.SkinManager.log("apply Divider Color")
                val sage = ColorDrawable(getColor())
                view.divider = sage
                view.dividerHeight = dividerHeight
            }

            if ("drawable" == attrValueTypeName) {
                com.umeox.skin.SkinManager.log("apply Divider Drawable")
                view.divider = getDrawable()
            }
        }
    }

}