package com.umeox.flow_event_bus.observeable

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.umeox.flow_event_bus.core.EventBusCore
import com.umeox.flow_event_bus.store.ApplicationScopeViewModelProvider
import kotlinx.coroutines.*

//全局事件监听
@MainThread
inline fun <reified T> LifecycleOwner.observeGlobalEvent(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
): Job {
    return ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .observeEvent(
            this,
            T::class.java.name,
            minActiveState,
            dispatcher,
            isSticky,
            onReceived
        )
}

//Fragment / Activity 等 Scope事件
@MainThread
inline fun <reified T> LifecycleOwner.observeScopeEvent(
    owner: ViewModelStoreOwner,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
): Job {
    return ViewModelProvider(owner).get(EventBusCore::class.java)
        .observeEvent(
            this,
            T::class.java.name,
            minActiveState,
            dispatcher,
            isSticky,
            onReceived
        )
}


