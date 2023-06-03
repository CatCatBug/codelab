package com.umeox.skin_lib.entity

import android.view.View

abstract class SkinAttr {

    protected val RES_TYPE_NAME_COLOR = "color"
    protected val RES_TYPE_NAME_DRAWABLE = "drawable"

    /**
     * name of the attr, ex: background or textSize or textColor
     */
    var attrName: String? = null

    /**
     * id of the attr value refered to, normally is [2130745655]
     */
    var attrValueRefId = 0

    /**
     * entry name of the value , such as [app_exit_btn_background]
     */
    var attrValueRefName: String? = null

    /**
     * type of the value , such as color or drawable
     */
    var attrValueTypeName: String? = null

    /**
     * Use to apply view with new TypedValue
     * @param view
     * @param tv
     */
    abstract fun apply(view: View)

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