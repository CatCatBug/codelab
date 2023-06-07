package com.umeox.skin.listener

interface ISkinUpdate {
    fun onThemeUpdate()
    fun onTextFontUpdate(replaceTable:Map<String,String>)
}