package cc.fastcv.jetpack.lifecycle

class CvReflectiveGenericLifecycleObserver(private val mWrapped: Any) : CvLifecycleEventObserver {

    private var mInfo: CvClassesInfoCache.CallbackInfo =
        CvClassesInfoCache.sInstance.getInfo(mWrapped::class.java)

    override fun onStateChanged(source: CvLifecycleOwner, event: CvLifecycle.Event) {
        mInfo.invokeCallbacks(source, event, mWrapped)
    }
}