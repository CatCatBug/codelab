package com.umeox.skin.entity

import android.view.View

class SkinItem {

    var view: View? = null

    var attrs = mutableListOf<Attr>()

    fun apply() {
        view?.let {
            for (attr in attrs) {
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