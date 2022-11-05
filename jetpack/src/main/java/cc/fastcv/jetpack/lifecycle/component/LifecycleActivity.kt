package cc.fastcv.jetpack.lifecycle.component

import android.app.Activity
import android.os.Bundle
import cc.fastcv.jetpack.lifecycle.*

open class LifecycleActivity : Activity(), CvLifecycleOwner {

    private val mLifecycleRegistry = CvLifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CvReportFragment.injectIfNeededIn(this)
        ViewTreeLifecycleOwner.set(window.decorView,this)
    }

    override fun getLifecycle(): CvLifecycle {
        return mLifecycleRegistry
    }

}