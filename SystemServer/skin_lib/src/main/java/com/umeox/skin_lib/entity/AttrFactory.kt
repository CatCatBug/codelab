package com.umeox.skin_lib.entity

import com.umeox.skin_lib.SkinManager

object AttrFactory {

    private fun getAttrByName(name: String): Attr? {
        return when (name) {
            "background" -> BackgroundAttr()
            "textColor" -> TextColorAttr()
            "listSelector" -> ListSelectorAttr()
            "divider" -> DividerAttr()
            "fontFamily" -> TextFontAttr()
            "backgroundTint" -> BackgroundTintAttr()
            else -> null
        }
    }

    /**
     * Check whether the attribute is supported
     * @param attrName
     * @return true : supported <br></br>
     * false: not supported
     */
    fun isSupportedAttr(attrName: String): Boolean {
        return getAttrByName(attrName) != null
    }

    fun get(
        attrName: String,
        attrValueRefId: Int,
        attrValueRefName: String,
        typeName: String
    ): Attr? {
        SkinManager.log("attrName = $attrName attrValueRefId = $attrValueRefId attrValueRefName = $attrValueRefName typeName = $typeName")
        val mSkinAttr = getAttrByName(attrName) ?: return null
        mSkinAttr.attrName = attrName
        mSkinAttr.attrValueRefId = attrValueRefId
        mSkinAttr.attrValueRefName = attrValueRefName
        mSkinAttr.attrValueTypeName = typeName
        return mSkinAttr
    }
}