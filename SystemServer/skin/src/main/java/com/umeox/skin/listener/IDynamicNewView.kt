package com.umeox.skin.listener

import android.view.View
import com.umeox.skin.entity.DynamicAttr

interface IDynamicNewView {
    fun dynamicAddView(view: View?, pDAttrs: List<DynamicAttr>)
}