package com.umeox.skin.listener

interface ISkinLoader {
    fun attach(observer: ISkinUpdate)
    fun detach(observer: ISkinUpdate)
    fun notifySkinUpdate()
}