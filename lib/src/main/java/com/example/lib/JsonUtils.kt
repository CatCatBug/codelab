package com.example.lib

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


/**
 * Json处理工具
 * 单独提出来为了方便后期更换序列化库
 *
 * gson库：implementation 'com.google.code.gson:gson:2.9.0'
 *
 */
object JsonUtils {

    private val gson by lazy {
        GsonBuilder()
            .disableHtmlEscaping()
            .create()
    }

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }

    fun <T> toObj(json: String): T {
        val type: Type = object : TypeToken<T>() {}.type
        return gson.fromJson(json, type)
    }

}