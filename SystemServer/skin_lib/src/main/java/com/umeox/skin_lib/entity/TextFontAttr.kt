package com.umeox.skin_lib.entity

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.TextView
import com.umeox.skin_lib.loader.SkinManager

class TextFontAttr : SkinAttr() {

    override fun apply(view: View) {
    }

    fun applyTextFont(view: View,replaceTable:Map<String,String>) {
        if ("font" == attrValueTypeName) {
            Log.i("xcl_debug", "apply fontFamily")
            val newFontName = replaceTable[attrValueRefName]
            Log.d("xcl_debug", "applyTextFont: newFontName = $newFontName")
            attrValueRefName = newFontName
            newFontName?.let {
                (view as TextView).typeface = SkinManager.getFont(it)
            }
        }
    }
}