package com.umeox.skin_lib.config

import com.umeox.skin_lib.entity.*

object AttrFactory {

    const val BACKGROUND = "background"
    const val TEXT_COLOR = "textColor"
    const val LIST_SELECTOR = "listSelector"
    const val DIVIDER = "divider"

    /**
     * Check whether the attribute is supported
     * @param attrName
     * @return true : supported <br></br>
     * false: not supported
     */
    fun isSupportedAttr(attrName: String): Boolean {
        if (BACKGROUND == attrName) {
            return true
        }
        if (TEXT_COLOR == attrName) {
            return true
        }
        if (LIST_SELECTOR == attrName) {
            return true
        }
        return DIVIDER == attrName
    }

    fun get(
        attrName: String,
        attrValueRefId: Int,
        attrValueRefName: String,
        typeName: String
    ): SkinAttr? {
        val mSkinAttr = if (BACKGROUND == attrName) {
            BackgroundAttr()
        } else if (TEXT_COLOR == attrName) {
            TextColorAttr()
        } else if (LIST_SELECTOR == attrName) {
            ListSelectorAttr()
        } else if (DIVIDER == attrName) {
            DividerAttr()
        } else {
            return null
        }
        mSkinAttr.attrName = attrName
        mSkinAttr.attrValueRefId = attrValueRefId
        mSkinAttr.attrValueRefName = attrValueRefName
        mSkinAttr.attrValueTypeName = typeName
        return mSkinAttr
    }
}