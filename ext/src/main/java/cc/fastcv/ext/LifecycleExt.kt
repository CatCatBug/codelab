package cc.fastcv.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Create by Eric
 * on 2023/1/7
 */
fun <T> LifecycleOwner.collect(
    flow: Flow<T>,
    block: (T?) -> Unit
) {
    lifecycleScope.launch {
        flow.collect {
            block(it)
        }
    }
}

fun <T> LifecycleOwner.collectWhenStart(
    flow: Flow<T>,
    block: (T?) -> Unit
) {
    flow
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach {
            block(it)
        }
        .launchIn(lifecycleScope)
}
