package cc.fastcv.jetpack.lifecycle

import androidx.annotation.NonNull

interface CvLifecycleOwner {
    @NonNull
    fun getLifecycle() : CvLifecycle
}