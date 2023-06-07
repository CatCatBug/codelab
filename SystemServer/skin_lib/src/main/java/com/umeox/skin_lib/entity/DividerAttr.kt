package com.umeox.skin_lib.entity

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ListView
import com.umeox.skin_lib.SkinManager

class DividerAttr : Attr() {
    private var dividerHeight = 1

    override fun apply(view: View) {
        if (view is ListView) {
            if ("color" == attrValueTypeName) {
                SkinManager.log("apply Divider Color")
                val sage = ColorDrawable(getColor())
                view.divider = sage
                view.dividerHeight = dividerHeight
            }

            if ("drawable" == attrValueTypeName) {
                SkinManager.log("apply Divider Drawable")
                view.divider = getDrawable()
            }
        }
    }

}