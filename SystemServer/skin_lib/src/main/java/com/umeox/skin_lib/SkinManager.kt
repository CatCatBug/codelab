package com.umeox.skin_lib

import android.app.Application
import android.util.Log
import com.umeox.skin_lib.ext.getSpStringValue
import com.umeox.skin_lib.ext.saveSpStringValue
import com.umeox.skin_lib.listener.ISkinLoader
import com.umeox.skin_lib.listener.ISkinUpdate

object SkinManager : ISkinLoader {

    private const val PREF_CUSTOM_SKIN_PATH = "skin_custom_path"
    private const val SKIN_SUFFIX = "skin_suffix"
    private const val DEFAULT_SKIN = ""

    private var loaded = false
    private lateinit var mode: SkinMode

    internal lateinit var app: Application

    private var skinObservers = mutableListOf<ISkinUpdate>()

    fun init(ctx: Application, mode: SkinMode) {
        app = ctx
        this.mode = mode
        loadResource(mode)
    }

    fun isDefaultSkin(): Boolean {
        val curSkin = if (mode == SkinMode.EXTERN_SKIN_MODE) {
            getCustomSkinPath()
        } else {
            getSkinSuffix()
        }
        return curSkin == DEFAULT_SKIN
    }

    fun getCurSkin(): String {
        return if (mode == SkinMode.EXTERN_SKIN_MODE) {
            getCustomSkinPath()
        } else {
            getSkinSuffix()
        }
    }

    fun restoreDefaultTheme() {
        if (mode == SkinMode.EXTERN_SKIN_MODE) {
            saveCustomSkinPath(DEFAULT_SKIN)
        } else {
            saveSkinSuffix(DEFAULT_SKIN)
        }
        ResourceManager.restoreDefaultTheme()
        notifySkinUpdate()
    }

    fun applyTextFont(replaceTable: Map<String, String>) {
        log("notifySkinUpdate: 通知字体更新")
        for (observer in skinObservers) {
            observer.onTextFontUpdate(replaceTable)
        }
    }

    fun applySkin(path: String) {
        log("notifySkinUpdate: 通知皮肤更新")

        if (path != DEFAULT_SKIN && path != getCurSkin()) {
            ResourceManager.loadResource(path, mode)
        }
    }

    fun getMode() = mode

    private fun loadResource(mode: SkinMode) {
        val path = if (mode == SkinMode.EXTERN_SKIN_MODE) {
            getCustomSkinPath()
        } else {
            getSkinSuffix()
        }

        if (path != DEFAULT_SKIN) {
            ResourceManager.loadResource(path, mode)
        }
    }

    internal fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("SuperSkinManager", msg)
        }
    }

    private fun getSkinSuffix(): String {
        return SKIN_SUFFIX.getSpStringValue(DEFAULT_SKIN)
    }


    private fun saveSkinSuffix(suffix: String) {
        SKIN_SUFFIX.saveSpStringValue(suffix)
    }

    private fun getCustomSkinPath(): String {
        return PREF_CUSTOM_SKIN_PATH.getSpStringValue(DEFAULT_SKIN)
    }

    private fun saveCustomSkinPath(path: String) {
        PREF_CUSTOM_SKIN_PATH.saveSpStringValue(path)
    }

    override fun attach(observer: ISkinUpdate) {
        if (!skinObservers.contains(observer)) {
            skinObservers.add(observer)
        }
    }

    override fun detach(observer: ISkinUpdate) {
        if (skinObservers.contains(observer)) {
            skinObservers.remove(observer)
        }
    }

    override fun notifySkinUpdate() {
        for (observer in skinObservers) {
            observer.onThemeUpdate()
        }
    }

    fun savePath(targetPath: String) {
        loaded = true
        if (mode == SkinMode.EXTERN_SKIN_MODE) {
            saveCustomSkinPath(targetPath)
        } else {
            saveSkinSuffix(targetPath)
        }
    }
}