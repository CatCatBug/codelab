package cc.fastcv.jetpack.lifecycle

import androidx.lifecycle.MethodCallsLogger

class CvCompositeGeneratedAdaptersObserver(private val mGeneratedAdapters: Array<CvGeneratedAdapter>) :
    CvLifecycleEventObserver {
    override fun onStateChanged(source: CvLifecycleOwner, event: CvLifecycle.Event) {
        val logger = CvMethodCallsLogger()
        for (mGeneratedAdapter in mGeneratedAdapters) {
            mGeneratedAdapter.callMethods(source, event, false, logger)
        }

        for (mGeneratedAdapter in mGeneratedAdapters) {
            mGeneratedAdapter.callMethods(source, event, true, logger)
        }
    }
}