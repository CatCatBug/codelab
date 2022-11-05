package cc.fastcv.jetpack.lifecycle

class CvFullLifecycleObserverAdapter(
    private val mFullLifecycleObserver: CvFullLifecycleObserver?,
    private val mLifecycleEventObserver: CvLifecycleEventObserver?
) : CvLifecycleEventObserver {

    override fun onStateChanged(source: CvLifecycleOwner, event: CvLifecycle.Event) {
        when(event) {
            CvLifecycle.Event.ON_CREATE -> mFullLifecycleObserver?.onCreate(source)
            CvLifecycle.Event.ON_START -> mFullLifecycleObserver?.onStart(source)
            CvLifecycle.Event.ON_RESUME -> mFullLifecycleObserver?.onResume(source)
            CvLifecycle.Event.ON_PAUSE -> mFullLifecycleObserver?.onPause(source)
            CvLifecycle.Event.ON_STOP -> mFullLifecycleObserver?.onStop(source)
            CvLifecycle.Event.ON_DESTROY -> mFullLifecycleObserver?.onDestroy(source)
            CvLifecycle.Event.ON_ANY -> throw IllegalArgumentException("ON_ANY must not been send by anybody")
        }
        mLifecycleEventObserver?.onStateChanged(source, event)
    }
}