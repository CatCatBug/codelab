package com.umeox.skin_lib.entity

import android.view.View
import com.umeox.skin_lib.SkinManager

class SkinItem {

    var view: View? = null

    var attrs = mutableListOf<Attr>()

    fun apply() {
        SkinManager.log("SkinItem:apply  view = $view")
        view?.let {
            for (attr in attrs) {
                SkinManager.log("SkinItem:apply  attr = $attr")
                attr.apply(it)
            }
        }
    }

    fun applyTextFont(replaceTable:Map<String,String>) {
        view?.let {
            for (attr in attrs) {
                attr.applyTextFont(it,replaceTable)
            }
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