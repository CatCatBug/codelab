package cc.fastcv.jetpack.lifecycle

class CvSingleGeneratedAdapterObserver(private val mGeneratedAdapter:CvGeneratedAdapter) : CvLifecycleEventObserver {

    override fun onStateChanged(source: CvLifecycleOwner, event: CvLifecycle.Event) {
        mGeneratedAdapter.callMethods(source,event,false,null)
        mGeneratedAdapter.callMethods(source,event,true,null)
    }
}