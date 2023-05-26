package com.umeox.flow_event_bus.core

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umeox.flow_event_bus.utils.launchWhenStateAtLeast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.lang.Exception

class EventBusCore : ViewModel() {

    //正常事件
    private val eventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    //粘性事件
    private val stickyEventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    private fun getEventFlow(eventName: String, isSticky: Boolean): MutableSharedFlow<Any> {
        return if (isSticky) {
            stickyEventFlows[eventName]
        } else {
            eventFlows[eventName]
        } ?: MutableSharedFlow<Any>(
            replay = if (isSticky) 1 else 0,
            extraBufferCapacity = Int.MAX_VALUE
        ).also {
            if (isSticky) {
                stickyEventFlows[eventName] = it
            } else {
                eventFlows[eventName] = it
            }
        }
    }

    fun <T : Any> observeEvent(
        lifecycleOwner: LifecycleOwner,
        eventName: String,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher,
        isSticky: Boolean,
        onReceived: (T) -> Unit
    ): Job {
        return lifecycleOwner.launchWhenStateAtLeast(minState) {
            getEventFlow(eventName, isSticky).collect {
                this.launch(dispatcher) {
                    invokeReceived(it, onReceived)
                }
            }
        }
    }

    fun postEvent(eventName: String, value: Any, timeMillis: Long) {
        listOfNotNull(
            getEventFlow(eventName, false),
            getEventFlow(eventName, true)
        ).forEach {
            viewModelScope.launch {
                delay(timeMillis)
                it.emit(value)
            }
        }
    }

    private fun <T : Any> invokeReceived(value: Any, onReceived: (T) -> Unit) {
        try {
            onReceived.invoke(value as T)
        } catch (e: ClassCastException) {
            Log.w("EventBusCore", "class cast error on message received: $value", e)
        } catch (e: Exception) {
            Log.w("EventBusCore", "error on message received: $value", e)
        }
    }

}