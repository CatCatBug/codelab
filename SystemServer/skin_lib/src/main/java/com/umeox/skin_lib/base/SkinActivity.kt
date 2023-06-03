package com.umeox.skin_lib.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umeox.skin_lib.entity.DynamicAttr
import com.umeox.skin_lib.listener.ISkinUpdate
import com.umeox.skin_lib.loader.SkinInflaterFactory
import com.umeox.skin_lib.loader.SkinManager

open class SkinActivity : AppCompatActivity(), ISkinUpdate {

    /**
     * Whether response to skin changing after create
     */
    private var isResponseOnSkinChanging = true

    private var mSkinInflaterFactory: SkinInflaterFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mSkinInflaterFactory = SkinInflaterFactory()
        layoutInflater.factory = mSkinInflaterFactory
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        SkinManager.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SkinManager.detach(this)
    }

    protected fun dynamicAddSkinEnableView(view: View, attrName: String?, attrValueResId: Int) {
        mSkinInflaterFactory?.dynamicAddSkinEnableView(this, view, attrName, attrValueResId)
    }

    protected fun dynamicAddSkinEnableView(view: View, pDAttrs: List<DynamicAttr>) {
        mSkinInflaterFactory?.dynamicAddSkinEnableView(this, view, pDAttrs)
    }

    protected fun enableResponseOnSkinChanging(enable: Boolean) {
        isResponseOnSkinChanging = enable
    }

    override fun onThemeUpdate() {
        if (!isResponseOnSkinChanging) return
        mSkinInflaterFactory?.applySkin()
    }

    override fun onTextFontUpdate(replaceTable:Map<String,String>) {
        if (!isResponseOnSkinChanging) return
        mSkinInflaterFactory?.applyTextFont(replaceTable)
    }

    fun dynamicAddView(view: View, pDAttrs: List<DynamicAttr>) {
        mSkinInflaterFactory?.dynamicAddSkinEnableView(this, view, pDAttrs)
    }

}