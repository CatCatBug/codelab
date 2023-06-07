package com.umeox.skin

import android.app.Application
import android.util.Log
import com.umeox.skin.ext.getSpStringValue
import com.umeox.skin.ext.saveSpStringValue
import com.umeox.skin.listener.ISkinLoader
import com.umeox.skin.listener.ISkinUpdate

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

    fun restoreDefaultTheme() {
        saveCustomSkinPath(DEFAULT_SKIN)
        ResourceManager.restoreDefaultTheme()
        notifySkinUpdate()
    }

    fun applyTextFont(replaceTable: Map<String, String>) {
        log("notifySkinUpdate: 通知字体更新 skinObservers = $skinObservers")
        for (observer in skinObservers) {
            observer.onTextFontUpdate(replaceTable)
        }
    }

    fun applySkin(path: String) {
        if (path != DEFAULT_SKIN) {
            ResourceManager.loadResource(path, mode)
        }
    }

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
        Log.d("SuperSkinManager", msg)
    }

    private fun getSkinSuffix(): String {
        return SKIN_SUFFIX.getSpStringValue(DEFAULT_SKIN)
    }


    fun saveSkinSuffix(suffix: String) {
        SKIN_SUFFIX.saveSpStringValue(suffix)
    }


    private fun getCustomSkinPath(): String {
        return PREF_CUSTOM_SKIN_PATH.getSpStringValue(DEFAULT_SKIN)
    }

    internal fun saveCustomSkinPath(path: String) {
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
        log("notifySkinUpdate: 通知皮肤更新 skinObservers = $skinObservers")
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