package com.umeox.skin

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.umeox.skin.listener.ILoaderListener

object ResourceManager : ILoaderListener {

    private val originResource = SkinManager.app.resources

    private var resourceLoader: ResourceLoader? = null

    private var targetPath = ""

    fun getColor(resId: Int): Int {
        val resName: String = originResource.getResourceEntryName(resId)
        return resourceLoader?.getColor(resName) ?: originResource.getColor(resId, null)
    }

    fun getDrawable(resId: Int): Drawable? {
        val resName: String = originResource.getResourceEntryName(resId)
        return resourceLoader?.getDrawable(resName)
            ?: ContextCompat.getDrawable(SkinManager.app, resId)
    }

    fun getColorStateList(resId: Int): ColorStateList? {
        val resName: String = originResource.getResourceEntryName(resId)
        return resourceLoader?.getColorStateList(resName)
            ?: ContextCompat.getColorStateList(SkinManager.app, resId)
    }

    fun getFont(name: String): Typeface? {
        return resourceLoader?.getFont(name)
    }

    internal fun loadResource(path: String, mode: SkinMode) {
        if (resourceLoader == null) {
            resourceLoader = if (mode == SkinMode.EXTERN_SKIN_MODE) {
                ExternResourceLoader()
            } else {
                InnerResourceLoader()
            }
        }
        targetPath = path
        resourceLoader!!.load(path, this)
    }

    override fun onStart() {
        SkinManager.log("皮肤资源加载开始")
    }

    override fun onSuccess() {
        SkinManager.log("皮肤资源加载成功")
        SkinManager.savePath(targetPath)
        SkinManager.notifySkinUpdate()
    }

    override fun onFailed() {
        SkinManager.log("皮肤资源加载失败")
    }

    fun restoreDefaultTheme() {
        resourceLoader = null
    }

}