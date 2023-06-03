package com.umeox.skin_lib.loader

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.umeox.skin_lib.config.SkinConfig
import com.umeox.skin_lib.ext.SkinCoroutineScope
import com.umeox.skin_lib.listener.ILoaderListener
import com.umeox.skin_lib.listener.ISkinLoader
import com.umeox.skin_lib.listener.ISkinUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@SuppressLint("StaticFieldLeak")
object SkinManager : ISkinLoader {

    /**
     * 设备携程空间
     */
    private val skinScope =
        SkinCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    internal var context: Context? = null
    private var skinPackageName: String? = null
    private var mResources: Resources? = null
    private var skinPath: String? = null
    private var isDefaultSkin = false

    private var skinObservers: MutableList<ISkinUpdate>? = null

    fun init(ctx: Context) {
        context = ctx.applicationContext
    }

    fun isExternalSkin(): Boolean {
        return true
    }

    /**
     * get current skin path
     * @return current skin path
     */
    fun getSkinPath(): String? {
        return skinPath
    }

    fun getSkinPackageName(): String? {
        return skinPackageName
    }

    fun getResources(): Resources? {
        return mResources
    }

    fun restoreDefaultTheme() {
        SkinConfig.saveSkinPath(SkinConfig.DEFALT_SKIN)
        isDefaultSkin = true
        mResources = context!!.resources
        notifySkinUpdate()
    }

    fun load() {
        val skin = SkinConfig.getCustomSkinPath()
        load(skin, null)
    }

    fun load(callback: ILoaderListener?) {
        val skin = SkinConfig.getCustomSkinPath()
        if (SkinConfig.isDefaultSkin()) {
            return
        }
        load(skin, callback)
    }

    fun load(skinPackagePath: String, callback: ILoaderListener?) {
        if (!SkinConfig.useSkinPackage) {
            Log.d("xcl_debug", "添加前缀： _$skinPackagePath")
            callback?.onStart()
            SkinConfig.saveSkinSuffix("_$skinPackagePath")
            callback?.onSuccess()
            notifySkinUpdate()
            return
        }

        Log.d("xcl_debug", "开始加载资源： $skinPackagePath")
        val startTime = System.currentTimeMillis()
        skinScope.launch(Dispatchers.IO) {
            callback?.onStart()
            val result: Resources? = try {
                val file = File(skinPackagePath)
                if (!file.exists()) {
                    Log.d("xcl_debug", "load: 文件不存在")
                    null
                } else {
                    val mPm = context!!.packageManager
                    val mInfo =
                        mPm.getPackageArchiveInfo(skinPackagePath, PackageManager.GET_ACTIVITIES)
                    skinPackageName = mInfo!!.packageName
                    val assetManager = AssetManager::class.java.newInstance()
                    val addAssetPath = assetManager.javaClass.getMethod(
                        "addAssetPath",
                        String::class.java
                    )
                    addAssetPath.invoke(assetManager, skinPackagePath)
                    val superRes = context!!.resources

                    context!!.createConfigurationContext(Configuration())

                    //TODO 后期需要想办法替换
                    val skinResource =
                        Resources(assetManager, superRes.displayMetrics, superRes.configuration)
                    SkinConfig.saveSkinPath(skinPackagePath)
                    skinPath = skinPackagePath
                    isDefaultSkin = false
                    skinResource
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            withContext(Dispatchers.Main) {
                mResources = result
                Log.d(
                    "xcl_debug",
                    "加载资源完成  mResources = $mResources，总用时：${System.currentTimeMillis() - startTime}"
                )
                if (mResources != null) {
                    callback?.onSuccess()
                    notifySkinUpdate()
                } else {
                    isDefaultSkin = true
                    callback?.onFailed()
                }
            }
        }
    }

    override fun attach(observer: ISkinUpdate) {
        if (skinObservers == null) {
            skinObservers = mutableListOf()
        }
        if (!skinObservers!!.contains(observer)) {
            skinObservers!!.add(observer)
        }
    }

    override fun detach(observer: ISkinUpdate) {
        if (skinObservers == null) return
        if (skinObservers!!.contains(observer)) {
            skinObservers!!.remove(observer)
        }
    }

    override fun notifySkinUpdate() {
        Log.d("xcl_debug", "notifySkinUpdate: 通知皮肤更新 skinObservers = $skinObservers")
        if (skinObservers == null) return
        for (observer in skinObservers!!) {
            observer.onThemeUpdate()
        }
    }

    fun getColor(resId: Int): Int {
        val originColor: Int = context!!.resources.getColor(resId, null)

        if (!SkinConfig.useSkinPackage) {
            val resName: String =
                context!!.resources.getResourceEntryName(resId) + SkinConfig.getSkinSuffix()

            val trueResId: Int =
                context!!.resources.getIdentifier(resName, "color", context!!.packageName)

            val trueColor: Int = try {
                context!!.resources.getColor(trueResId, null)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
                originColor
            }
            return trueColor
        } else {
            if (mResources == null || isDefaultSkin) {
                return originColor
            }

            val resName: String = context!!.resources.getResourceEntryName(resId)

            val trueResId: Int = mResources!!.getIdentifier(resName, "color", skinPackageName)

            val trueColor: Int = try {
                mResources!!.getColor(trueResId, null)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
                originColor
            }

            return trueColor
        }
    }

    fun getDrawable(resId: Int): Drawable? {
        val originDrawable = ContextCompat.getDrawable(context!!, resId)
        if (!SkinConfig.useSkinPackage) {
            val resName =
                context!!.resources.getResourceEntryName(resId) + SkinConfig.getSkinSuffix()

            val trueResId = context!!.resources.getIdentifier(resName, "drawable", skinPackageName)

            val trueDrawable: Drawable? = try {
                ResourcesCompat.getDrawable(context!!.resources, trueResId, null)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
                originDrawable
            }

            Log.d(
                "xcl_debug",
                "getDrawable: originDrawable = $originDrawable    trueDrawable = $trueDrawable"
            )
            return trueDrawable
        } else {
            if (mResources == null || isDefaultSkin) {
                return originDrawable
            }
            val resName = context!!.resources.getResourceEntryName(resId)

            val trueResId = mResources!!.getIdentifier(resName, "drawable", skinPackageName)

            val trueDrawable: Drawable? = try {
                ResourcesCompat.getDrawable(mResources!!, trueResId, null)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
                originDrawable
            }

            Log.d(
                "xcl_debug",
                "getDrawable: originDrawable = $originDrawable    trueDrawable = $trueDrawable"
            )
            return trueDrawable
        }
    }

    fun convertToColorStateList(resId: Int): ColorStateList? {
        var isExtendSkin = true

        if (mResources == null || isDefaultSkin) {
            isExtendSkin = false
        }

        if (!SkinConfig.useSkinPackage) {
            val resName =
                context!!.resources.getResourceEntryName(resId) + SkinConfig.getSkinSuffix()

            val trueResId =
                context!!.resources.getIdentifier(resName, "color", context!!.packageName)

            try {
                return ContextCompat.getColorStateList(context!!, trueResId)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

            try {
                return ContextCompat.getColorStateList(context!!, resId)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

            val states = Array(1) {
                IntArray(
                    1
                )
            }
            return ColorStateList(states, intArrayOf(ContextCompat.getColor(context!!, resId)))

        } else {
            val resName = context!!.resources.getResourceEntryName(resId)
            if (isExtendSkin) {
                val trueResId = mResources!!.getIdentifier(resName, "color", skinPackageName)
                val trueColorList: ColorStateList?
                // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                if (trueResId == 0) {
                    try {
                        return ContextCompat.getColorStateList(context!!, resId)
                    } catch (e: Resources.NotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        trueColorList =
                            ResourcesCompat.getColorStateList(mResources!!, trueResId, null)
                        return trueColorList
                    } catch (e: Resources.NotFoundException) {
                        e.printStackTrace()
                    }
                }
            } else {
                try {
                    return ContextCompat.getColorStateList(context!!, resId)
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }
            }

            val states = Array(1) {
                IntArray(
                    1
                )
            }
            return ColorStateList(states, intArrayOf(ContextCompat.getColor(context!!, resId)))
        }
    }
}