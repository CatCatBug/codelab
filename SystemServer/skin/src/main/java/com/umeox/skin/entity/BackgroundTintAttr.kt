package com.umeox.skin.entity

import android.view.View

class BackgroundTintAttr : Attr() {

    override fun apply(view: View) {
        com.umeox.skin.SkinManager.log("apply backgroundTintList")
        view.backgroundTintList = getColorStateList()
    }

}