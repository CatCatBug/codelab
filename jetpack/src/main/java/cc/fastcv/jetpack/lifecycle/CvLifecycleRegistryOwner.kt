package cc.fastcv.jetpack.lifecycle

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner

interface CvLifecycleRegistryOwner : CvLifecycleOwner {
    @NonNull
    override fun getLifecycle() : CvLifecycleRegistry
}