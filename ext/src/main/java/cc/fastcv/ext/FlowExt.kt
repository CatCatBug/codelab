package cc.fastcv.ext

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Create by Eric
 * on 2023/1/7
 */

/**
 * 获取一个flow,当有新接收者时会收到最近一条数据
 */
fun <T: Any> obtainSharedFlow() = MutableSharedFlow<T>(replay = 1)

/**
 * 获取一个flow,每次都会接收新数据
 */
fun <T: Any> obtainStateFlow() = MutableStateFlow(Any())
