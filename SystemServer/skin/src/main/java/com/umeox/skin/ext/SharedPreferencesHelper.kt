package com.umeox.skin.ext

import android.content.Context
import android.content.SharedPreferences
import com.umeox.skin.SkinManager

/**
 * SharedPreferences帮助类
 * 单例类
 */
object SharedPreferencesHelper {
    /**
     * 存储sp文件的名字
     */
    private const val SP_NAME = "skin"

    /**
     * SharedPreferences实例对象
     */
    private val mSharedPreferences: SharedPreferences by lazy {
        SkinManager.app.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 存入String值
     */
    fun put(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * 存入Int值
     */
    fun put(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    /**
     * 存入Long值
     */
    fun put(key: String, value: Long) {
        mSharedPreferences.edit().putLong(key, value).apply()
    }

    /**
     * 存入Float值
     */
    fun put(key: String, value: Float) {
        mSharedPreferences.edit().putFloat(key, value).apply()
    }

    /**
     * 存入Boolean值
     */
    fun put(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * 存入Set<String?>值
     */
    fun put(key: String, values: Set<String?>) {
        mSharedPreferences.edit().putStringSet(key, values).apply()
    }

    /**
     * 获取String值
     */
    fun getString(key: String, defaultValue: String): String {
        return mSharedPreferences.getString(key, defaultValue)?:defaultValue
    }

    /**
     * 获取Int值
     */
    fun getInt(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    /**
     * 获取Long值
     */
    fun getLong(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    /**
     * 获取Float值
     */
    fun getFloat(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    /**
     * 获取Boolean值
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * 获取Set<String?>值
     */
    fun getStringSet(key: String, defaultValue: Set<String?>): Set<String>? {
        return mSharedPreferences.getStringSet(key, defaultValue)
    }

    /**
     * 判断是否包含key值
     */
    fun contains(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

    /**
     * 移除key值及对应的value值
     */
    fun remove(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }

    /**
     * 清空sp中的数据
     */
    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }
}