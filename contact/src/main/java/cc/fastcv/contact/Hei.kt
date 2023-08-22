package cc.fastcv.contact

public inline fun <R> suspend1(noinline block: suspend () -> R): suspend () -> R = block