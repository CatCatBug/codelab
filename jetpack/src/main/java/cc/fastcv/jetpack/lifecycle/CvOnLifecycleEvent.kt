package cc.fastcv.jetpack.lifecycle

import androidx.lifecycle.Lifecycle

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class CvOnLifecycleEvent(val value: CvLifecycle.Event)