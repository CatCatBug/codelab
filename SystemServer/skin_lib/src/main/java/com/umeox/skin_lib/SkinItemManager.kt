package com.umeox.skin_lib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.umeox.skin_lib.entity.Attr
import com.umeox.skin_lib.entity.AttrFactory
import com.umeox.skin_lib.entity.DynamicAttr
import com.umeox.skin_lib.entity.SkinItem

class SkinItemManager {

    /**
     * Store the view item that need skin changing in the activity
     */
    private val mSkinItems = mutableListOf<SkinItem>()

    private val inflaterFactory = SuperSkinInflaterFactory(this)

    fun getInflaterFactory(): LayoutInflater.Factory2 {
        return inflaterFactory
    }

    fun applySkin() {
        SkinManager.log("applySkin: 应用皮肤 mSkinItems.size = ${mSkinItems.size}")
        for (item in mSkinItems) {
            item.apply()
        }
    }

    fun applyTextFont(replaceTable:Map<String,String>) {
        SkinManager.log("applySkin: 应用字体 mSkinItems.size = ${mSkinItems.size}")
        for (item in mSkinItems) {
            item.applyTextFont(replaceTable)
        }
    }

    fun dynamicAddSkinEnableView(
        context: Context,
        view: View,
        attrName: String?,
        attrValueResId: Int
    ) {
        val entryName = context.resources.getResourceEntryName(attrValueResId)
        val typeName = context.resources.getResourceTypeName(attrValueResId)
        val skinAttr = AttrFactory.get(attrName!!, attrValueResId, entryName, typeName)
        if (skinAttr == null) {
            SkinManager.log("dynamicAddSkinEnableView: mSkinAttr = null ")
            return
        }
        dynamicAddSkinEnableView(context, view, arrayListOf(DynamicAttr(attrName, attrValueResId)))

    }

    fun dynamicAddSkinEnableView(context: Context, view: View, pDAttrs: List<DynamicAttr>) {
        if (pDAttrs.isEmpty()) {
            SkinManager.log("dynamicAddSkinEnableView: mSkinAttr = null ")
            return
        }
        addSkinAttr(context, pDAttrs, view)
    }

    internal fun parseSkinAttr(context: Context, attrs: AttributeSet, view: View) {
        val viewAttrs: MutableList<Attr> = ArrayList()
        for (i in 0 until attrs.attributeCount) {
            val skinAttr: Attr? = buildSkinAttr(context, attrs, i)
            if (skinAttr != null) {
                viewAttrs.add(skinAttr)
            }
        }

        if (viewAttrs.isNotEmpty()) {
            val skinItem = SkinItem()
            skinItem.view = view
            skinItem.attrs = viewAttrs
            mSkinItems.add(skinItem)
        }
    }

    private fun buildSkinAttr(context: Context, attrs: AttributeSet, index: Int): Attr? {
        val attrName = attrs.getAttributeName(index)
        if (!AttrFactory.isSupportedAttr(attrName)) {
            return null
        }

        val attrValue = attrs.getAttributeValue(index)

        return if (attrValue.startsWith("@") || attrName == "fontFamily") {
            try {
                val id = attrValue.substring(1).toInt()
                val entryName = context.resources.getResourceEntryName(id)
                val typeName = context.resources.getResourceTypeName(id)
                val mSkinAttr: Attr? = AttrFactory.get(attrName, id, entryName, typeName)

                mSkinAttr
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    private fun addSkinAttr(context: Context, pDAttrs: List<DynamicAttr>, view: View?) {
        val viewAttrs = mutableListOf<Attr>()
        val skinItem = SkinItem()
        skinItem.view = view
        for (dAttr in pDAttrs) {
            val id: Int = dAttr.refResId
            val entryName = context.resources.getResourceEntryName(id)
            val typeName = context.resources.getResourceTypeName(id)
            val mSkinAttr = AttrFactory.get(dAttr.attrName, id, entryName, typeName)
            if (mSkinAttr != null) {
                viewAttrs.add(mSkinAttr)
            } else {
                SkinManager.log("dynamicAddSkinEnableView: mSkinAttr = null ")
            }
        }
        skinItem.attrs = viewAttrs
        mSkinItems.add(skinItem)
    }

}