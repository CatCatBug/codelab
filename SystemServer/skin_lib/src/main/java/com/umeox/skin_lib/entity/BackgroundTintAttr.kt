package com.umeox.skin_lib.entity

import android.view.View
import com.umeox.skin_lib.SkinManager

class BackgroundTintAttr : Attr() {

    override fun apply(view: View) {
        SkinManager.log("apply backgroundTintList")
        view.backgroundTintList = getColorStateList()
    }

}