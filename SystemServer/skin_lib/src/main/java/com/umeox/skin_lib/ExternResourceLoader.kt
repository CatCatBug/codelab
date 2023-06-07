package com.umeox.skin_lib

import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import com.umeox.skin_lib.ext.SkinCoroutineScope
import com.umeox.skin_lib.listener.ILoaderListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ExternResourceLoader : ResourceLoader() {

    var skinPackageName = ""
    var skinPath: String = ""
    var mResources: Resources? = null

    /**
     * 设备携程空间
     */
    private val skinScope =
        SkinCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun getColor(resName: String): Int? {
        if (mResources == null) {
            return null
        }
        val resId = mResources!!.getIdentifier(
            resName, "color",
            skinPackageName
        )
        return try {
            mResources!!.getColor(resId, null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getDrawable(resName: String): Drawable? {
        if (mResources == null) {
            return null
        }
        val resId = mResources!!.getIdentifier(
            resName, "drawable",
            skinPackageName
        )
        return try {
            ResourcesCompat.getDrawable(mResources!!, resId, null)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    override fun getColorStateList(resName: String): ColorStateList? {
        if (mResources == null) {
            return null
        }
        val resId = mResources!!.getIdentifier(
            resName, "color",
            skinPackageName
        )
        return try {
            ResourcesCompat.getColorStateList(
                mResources!!,
                resId,
                null
            )
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun getFont(resName: String): Typeface? {
        if (mResources == null) {
            return null
        }
        val trueResId = mResources!!.getIdentifier(resName, "font", skinPackageName)
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mResources!!.getFont(trueResId)
            } else {
                return null
            }
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun load(skinPackagePath: String, callback: ILoaderListener?) {
        SkinManager.log("开始加载资源： $skinPackagePath")
        val startTime = System.currentTimeMillis()
        skinScope.launch(Dispatchers.IO) {
            callback?.onStart()
            val result: Resources? = try {
                val file = File(skinPackagePath)
                if (!file.exists()) {
                    SkinManager.log("load: 文件不存在")
                    null
                } else {
                    val mPm = SkinManager.app.packageManager
                    val mInfo =
                        mPm.getPackageArchiveInfo(skinPackagePath, PackageManager.GET_ACTIVITIES)
                    skinPackageName = mInfo!!.packageName
                    val assetManager = AssetManager::class.java.newInstance()
                    val addAssetPath = assetManager.javaClass.getMethod(
                        "addAssetPath",
                        String::class.java
                    )
                    addAssetPath.invoke(assetManager, skinPackagePath)
                    val superRes = SkinManager.app.resources

                    //TODO 后期需要想办法替换
                    val skinResource =
                        Resources(assetManager, superRes.displayMetrics, superRes.configuration)
                    skinPath = skinPackagePath
                    skinResource
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            withContext(Dispatchers.Main) {
                mResources = result
                SkinManager.log(
                    "加载资源完成  mResources = ${mResources}，总用时：${System.currentTimeMillis() - startTime}"
                )
                if (mResources != null) {
                    callback?.onSuccess()
                } else {
                    callback?.onFailed()
                }
            }
        }
    }

}