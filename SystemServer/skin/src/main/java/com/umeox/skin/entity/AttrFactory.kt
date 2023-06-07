package com.umeox.skin.entity


object AttrFactory {

    enum class AttrImpl(val attrName: String, val attr: Attr) {
        BACKGROUND("background", BackgroundAttr()), TEXT_COLOR(
            "textColor",
            TextColorAttr()
        ),
        LIST_SELECTOR("listSelector", ListSelectorAttr()), DIVIDER(
            "divider",
            DividerAttr()
        ),
        FONT_FAMILY(
            "fontFamily", TextFontAttr()
        )
    }

    private fun getAttrByName(name: String): AttrImpl? {
        return when (name) {
            AttrImpl.BACKGROUND.name -> AttrImpl.BACKGROUND
            AttrImpl.TEXT_COLOR.name -> AttrImpl.TEXT_COLOR
            AttrImpl.LIST_SELECTOR.name -> AttrImpl.LIST_SELECTOR
            AttrImpl.DIVIDER.name -> AttrImpl.DIVIDER
            AttrImpl.FONT_FAMILY.name -> AttrImpl.FONT_FAMILY
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
        val mSkinAttr = getAttrByName(attrName)?.attr ?: return null
        mSkinAttr.attrName = attrName
        mSkinAttr.attrValueRefId = attrValueRefId
        mSkinAttr.attrValueRefName = attrValueRefName
        mSkinAttr.attrValueTypeName = typeName
        return mSkinAttr
    }
}