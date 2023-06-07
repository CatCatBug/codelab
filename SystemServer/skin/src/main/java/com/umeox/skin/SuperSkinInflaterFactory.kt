package com.umeox.skin

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

private const val NAMESPACE = "http://schemas.android.com/android/skin"
private const val ATTR_SKIN_ENABLE = "skinAble"
class SuperSkinInflaterFactory(private val itemManager: SkinItemManager) : LayoutInflater.Factory2 {


    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return createView(context, name, attrs)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return createView(context, name, attrs)
    }

    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        //if this is NOT enable to be skined , simplly skip it
        val isSkinEnable =
            attrs.getAttributeBooleanValue(NAMESPACE, ATTR_SKIN_ENABLE, false)

        if (!isSkinEnable) {
            return null
        }

        val view: View = createViewInner(context, name, attrs) ?: return null
        itemManager.parseSkinAttr(context, attrs, view)
        return view
    }

    private fun createViewInner(context: Context, name: String, attrs: AttributeSet): View? {
        SkinManager.log("createViewInner name =  $name")
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
            SkinManager.log("error while create 【" + name + "】 : " + e.message)
            view = null
        }
        SkinManager.log("create result: view = $view")
        return view
    }
}