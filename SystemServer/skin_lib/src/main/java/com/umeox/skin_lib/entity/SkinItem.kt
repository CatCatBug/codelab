package com.umeox.skin_lib.entity

import android.view.View

class SkinItem {


    var view: View? = null

    var attrs = mutableListOf<SkinAttr>()

    fun apply() {
        if (attrs.isEmpty()) {
            return
        }
        for (at in attrs) {
            at.apply(view!!)
        }
    }

    fun clean() {
        if (attrs.isEmpty()) {
            return
        }
        attrs.clear()
    }

    override fun toString(): String {
        return "SkinItem [view=" + view!!.javaClass.simpleName + ", attrs=" + attrs + "]"
    }

}