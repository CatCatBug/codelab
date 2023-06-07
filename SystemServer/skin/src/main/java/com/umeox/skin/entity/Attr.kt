package com.umeox.skin.entity

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import com.umeox.skin.ResourceManager

abstract class Attr {

    //name of the attr, ex: background or textSize or textColor
    var attrName: String? = null

    //id of the attr value refered to, normally is [2130745655]
    var attrValueRefId = 0

    //entry name of the value , such as [app_exit_btn_background]
    var attrValueRefName: String? = null

    //type of the value , such as color or drawable
    var attrValueTypeName: String? = null

    //Use to apply view with new TypedValue
    abstract fun apply(view: View)

    //Use to apply view with new TextFont
    open fun applyTextFont(view: View, replaceTable: Map<String, String>) {}

    //获取当前皮肤的颜色资源
    fun getColor(): Int {
        return ResourceManager.getColor(attrValueRefId)
    }

    fun getDrawable(): Drawable? {
        return ResourceManager.getDrawable(attrValueRefId)
    }

    fun getColorStateList(): ColorStateList? {
        return ResourceManager.getColorStateList(attrValueRefId)
    }

    fun getTextFont(): Typeface? {
        return attrValueRefName?.let {
            ResourceManager.getFont(it)
        }
    }

    override fun toString(): String {
        return """
             SkinAttr 
             [
             attrName=$attrName, 
             attrValueRefId=$attrValueRefId, 
             attrValueRefName=$attrValueRefName, 
             attrValueTypeName=$attrValueTypeName
             ]
             """.trimIndent()
    }

}