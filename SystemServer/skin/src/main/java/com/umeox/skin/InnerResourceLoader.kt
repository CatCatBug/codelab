package com.umeox.skin

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.umeox.skin.listener.ILoaderListener

class InnerResourceLoader : ResourceLoader() {

    private var suffix = ""

    private val mResources = SkinManager.app.resources
    private val skinPackageName = SkinManager.app.packageName

    override fun getColor(resName: String): Int? {
        val resId = mResources!!.getIdentifier(
            resName, "color",
            skinPackageName
        )
        return try {
            mResources.getColor(resId, null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getDrawable(resName: String): Drawable? {
        val resId = mResources.getIdentifier(
            resName, "drawable",
            skinPackageName
        )
        return try {
            ResourcesCompat.getDrawable(mResources, resId, null)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun getColorStateList(resName: String): ColorStateList? {
        val resId = mResources.getIdentifier(
            resName, "color",
            skinPackageName
        )
        return try {
            ResourcesCompat.getColorStateList(
                mResources,
                resId,
                null
            )
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun getFont(resName: String): Typeface? {
        val trueResId = mResources.getIdentifier(resName, "font", skinPackageName)
        return try {
            ResourcesCompat.getFont(SkinManager.app, trueResId)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun load(skinPackagePath: String, callback: ILoaderListener?) {
        callback?.onStart()
        suffix = skinPackagePath
        callback?.onSuccess()
    }

}