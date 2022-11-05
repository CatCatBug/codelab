package cc.fastcv.jetpack.lifecycle

interface CvFullLifecycleObserver : CvLifecycleObserver {

    fun onCreate(owner: CvLifecycleOwner)

    fun onStart(owner: CvLifecycleOwner)

    fun onResume(owner: CvLifecycleOwner)

    fun onPause(owner: CvLifecycleOwner)

    fun onStop(owner: CvLifecycleOwner)

    fun onDestroy(owner: CvLifecycleOwner)

}