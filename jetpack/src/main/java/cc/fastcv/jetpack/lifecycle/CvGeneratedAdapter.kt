package cc.fastcv.jetpack.lifecycle

import androidx.lifecycle.MethodCallsLogger

interface CvGeneratedAdapter {
    fun callMethods(
        source: CvLifecycleOwner,
        event: CvLifecycle.Event,
        onAny: Boolean,
        logger: CvMethodCallsLogger?
    )
}