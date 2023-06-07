package com.umeox.skin

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import com.umeox.skin.listener.ILoaderListener

abstract class ResourceLoader {

    abstract fun getFont(resName: String): Typeface?

    abstract fun getColorStateList(resName: String): ColorStateList?

    abstract fun getDrawable(resName: String): Drawable?

    abstract fun getColor(resName: String): Int?

    abstract fun load(skinPackagePath: String, callback: ILoaderListener?)

}