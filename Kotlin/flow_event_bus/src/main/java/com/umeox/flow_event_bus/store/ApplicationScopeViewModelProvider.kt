package com.umeox.flow_event_bus.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.umeox.flow_event_bus.EventBusInitializer

object ApplicationScopeViewModelProvider : ViewModelStoreOwner {

    private val eventViewModelStore: ViewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return eventViewModelStore
    }

    private val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            ApplicationScopeViewModelProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(EventBusInitializer.application)
        )
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return mApplicationProvider[modelClass]
    }

}