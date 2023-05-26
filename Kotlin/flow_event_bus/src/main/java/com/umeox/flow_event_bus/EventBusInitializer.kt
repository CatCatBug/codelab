package com.umeox.flow_event_bus

import android.app.Application

object EventBusInitializer {

    lateinit var application: Application

    fun init(application: Application) {
        EventBusInitializer.application = application
    }

}