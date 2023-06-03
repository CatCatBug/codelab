package com.umeox.skin_lib.listener

import android.view.View
import com.umeox.skin_lib.entity.DynamicAttr

interface IDynamicNewView {
    fun dynamicAddView(view: View?, pDAttrs: List<DynamicAttr>)
}