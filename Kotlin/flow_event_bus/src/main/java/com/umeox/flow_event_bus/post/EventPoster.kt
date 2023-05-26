package com.umeox.flow_event_bus.post

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.umeox.flow_event_bus.core.EventBusCore
import com.umeox.flow_event_bus.store.ApplicationScopeViewModelProvider

inline fun <reified T> postGlobalEvent(event: T, timeMillis: Long = 0L) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .postEvent(T::class.java.name, event!!, timeMillis)
}

inline fun <reified T> ViewModelStoreOwner.postEvent(event: T, timeMillis: Long = 0L) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .postEvent(T::class.java.name, event!!, timeMillis)
}