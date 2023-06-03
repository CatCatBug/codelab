package com.umeox.skin_lib.listener

interface ISkinLoader {
    fun attach(observer: ISkinUpdate)
    fun detach(observer: ISkinUpdate)
    fun notifySkinUpdate()
}