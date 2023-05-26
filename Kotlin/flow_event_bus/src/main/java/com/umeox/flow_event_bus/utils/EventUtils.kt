package com.umeox.flow_event_bus.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


fun <T> LifecycleOwner.launchWhenStateAtLeast(
    minState: Lifecycle.State,
    block: suspend CoroutineScope.() -> T
): Job {
    return lifecycleScope.launch {
        lifecycle.whenStateAtLeast(minState, block)
    }
}