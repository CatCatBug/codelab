package com.umeox.skin_lib.loader

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.umeox.skin_lib.config.AttrFactory
import com.umeox.skin_lib.config.SkinConfig
import com.umeox.skin_lib.entity.DynamicAttr
import com.umeox.skin_lib.entity.SkinAttr
import com.umeox.skin_lib.entity.SkinItem

class SkinInflaterFactory : LayoutInflater.Factory2 {

    private val TAG = "xcl_debug"

    /**
     * Store the view item that need skin changing in the activity
     */
    private val mSkinItems = mutableListOf<SkinItem>()
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        //if this is NOT enable to be skined , simplly skip it
        val isSkinEnable =
            attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false)

        if (!isSkinEnable) {
            return null
        }

        val view: View = createView(context, name, attrs) ?: return null

        parseSkinAttr(context, attrs, view)

        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        //if this is NOT enable to be skined , simplly skip it
        val isSkinEnable =
            attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false)

        if (!isSkinEnable) {
            return null
        }

        val view: View = createView(context, name, attrs) ?: return null

        parseSkinAttr(context, attrs, view)

        return view
    }

    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        Log.i(TAG, "about to create $name")
        var view: View? = null
        try {
            if (-1 == name.indexOf('.')) {
                if ("View" == name) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs)
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs)
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs)
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs)
            }
        } catch (e: Exception) {
            Log.e(TAG, "error while create 【" + name + "】 : " + e.message)
            view = null
        }
        Log.d(TAG, "create result: view = $view")
        return view
    }

    private fun parseSkinAttr(context: Context, attrs: AttributeSet, view: View) {
        val viewAttrs: MutableList<SkinAttr> = ArrayList()
        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            val attrValue = attrs.getAttributeValue(i)
            if (!AttrFactory.isSupportedAttr(attrName)) {
                continue
            }
            Log.d("xcl_debug", "parseSkinAttr: attrName = $attrName")
            if (attrValue.startsWith("@") || attrName == "fontFamily") {
                try {
                    val id = attrValue.substring(1).toInt()
                    val entryName = context.resources.getResourceEntryName(id)
                    val typeName = context.resources.getResourceTypeName(id)
                    val mSkinAttr: SkinAttr? = AttrFactory.get(attrName, id, entryName, typeName)
                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        if (viewAttrs.isNotEmpty()) {
            val skinItem = SkinItem()
            skinItem.view = view
            skinItem.attrs = viewAttrs
            mSkinItems.add(skinItem)
            if (SkinManager.isExternalSkin()) {
                skinItem.apply()
            }
        }
    }

    fun applySkin() {
        Log.d("xcl_debug", "applySkin: 应用皮肤")
        if (mSkinItems.isNotEmpty()) {
            for (si in mSkinItems) {
                if (si.view == null) {
                    continue
                }
                si.apply()
            }
        }
    }

    fun applyTextFont(replaceTable:Map<String,String>) {
        Log.d("xcl_debug", "applySkin: 应用字体")
        if (mSkinItems.isNotEmpty()) {
            for (si in mSkinItems) {
                if (si.view == null) {
                    continue
                }
                si.applyTextFont(replaceTable)
            }
        }
    }

    fun dynamicAddSkinEnableView(context: Context, view: View, pDAttrs: List<DynamicAttr>) {
        val viewAttrs = mutableListOf<SkinAttr>()
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
                Log.d("xcl_debug", "dynamicAddSkinEnableView: mSkinAttr = null ")
            }
        }
        skinItem.attrs = viewAttrs
        addSkinView(skinItem)
    }

    fun dynamicAddSkinEnableView(
        context: Context,
        view: View?,
        attrName: String?,
        attrValueResId: Int
    ) {
        val entryName = context.resources.getResourceEntryName(attrValueResId)
        val typeName = context.resources.getResourceTypeName(attrValueResId)
        val mSkinAttr = AttrFactory.get(attrName!!, attrValueResId, entryName, typeName)
        if (mSkinAttr == null) {
            Log.d("xcl_debug", "dynamicAddSkinEnableView: mSkinAttr = null ")
            return
        }

        val skinItem = SkinItem()
        skinItem.view = view
        val viewAttrs = mutableListOf<SkinAttr>()
        viewAttrs.add(mSkinAttr)
        skinItem.attrs = viewAttrs
        addSkinView(skinItem)
    }

    private fun addSkinView(item: SkinItem) {
        mSkinItems.add(item)
    }

}