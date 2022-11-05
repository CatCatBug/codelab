package cc.fastcv.jetpack.lifecycle

interface CvLifecycleEventObserver : CvLifecycleObserver {
    fun onStateChanged(source:CvLifecycleOwner, event: CvLifecycle.Event)
}