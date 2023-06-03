package com.umeox.skin_lib.listener

interface ISkinUpdate {
    fun onThemeUpdate()
    fun onTextFontUpdate(replaceTable:Map<String,String>)
}