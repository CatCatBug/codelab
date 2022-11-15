package cc.fastcv.jetpack.component

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import cc.fastcv.jetpack.lifecycle.*
import cc.fastcv.jetpack.viewmodel.*

open class BaseActivity : Activity(), CvLifecycleOwner, CvViewModelStoreOwner,
    CvHasDefaultViewModelProviderFactory {

    class NonConfigurationInstances {
        var custom: Any? = null
        var viewModelStore: CvViewModelStore? = null
    }

    private val mLifecycleRegistry = CvLifecycleRegistry(this)

    private var mViewModelStore: CvViewModelStore? = null
    private var mDefaultFactory: CvViewModelProvider.Factory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CvReportFragment.injectIfNeededIn(this)
        ViewTreeLifecycleOwner.set(window.decorView, this)
        CvViewTreeViewModelStoreOwner.set(window.decorView, this)
    }

    override fun getLifecycle(): CvLifecycle {
        return mLifecycleRegistry
    }

    override fun getViewModelStore(): CvViewModelStore {
        checkNotNull(application) {
            throw IllegalStateException(
                "Your activity is not yet attached to the "
                        + "Application instance. You can't request ViewModel before onCreate call."
            )
        }
        ensureViewModelStore()
        return mViewModelStore!!
    }

    private fun ensureViewModelStore() {
        if (mViewModelStore == null) {
            val nc: NonConfigurationInstances? =
                lastNonConfigurationInstance as NonConfigurationInstances?
            if (nc != null) {
                mViewModelStore = nc.viewModelStore
            }
            if (mViewModelStore == null) {
                mViewModelStore = CvViewModelStore()
            }
        }
    }

    override fun onRetainNonConfigurationInstance(): Any? {
        val custom = onRetainCustomNonConfigurationInstance()

        var viewModelStore = mViewModelStore
        if (viewModelStore == null) {
            val nc: NonConfigurationInstances? =
                lastNonConfigurationInstance as NonConfigurationInstances?
            if (nc != null) {
                viewModelStore = nc.viewModelStore
            }
        }

        if (viewModelStore == null && custom == null) {
            return null
        }

        val nci = NonConfigurationInstances()
        nci.custom = custom
        nci.viewModelStore = viewModelStore
        return nci
    }

    open fun onRetainCustomNonConfigurationInstance(): Any? {
        return null
    }

    fun getLastCustomNonConfigurationInstance(): Any? {
        val nc : NonConfigurationInstances? = lastNonConfigurationInstance as NonConfigurationInstances?
        return nc?.custom
    }

    override fun getDefaultViewModelProviderFactory(): CvViewModelProvider.Factory {
        if (application == null) {
            throw IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.")
        }
        if (mDefaultFactory == null) {
            mDefaultFactory = CvViewModelProvider.CvNewInstanceFactory()
        }
        return mDefaultFactory!!
    }

}