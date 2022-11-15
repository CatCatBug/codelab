package cc.fastcv.jetpack.viewmodel

import android.app.Application
import java.lang.reflect.InvocationTargetException

class CvViewModelProvider(private var mViewModelStore: CvViewModelStore, private var mFactory: Factory) {

    companion object {
        private const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"
    }

    interface Factory {
        fun <T : CvViewModel?> create(modelClass: Class<T>): T
    }

    open class OnRequeryFactory {
        fun onRequery(viewModel: CvViewModel?) {
        }
    }

    abstract class KeyedFactory : OnRequeryFactory(), Factory {
        abstract fun <T : CvViewModel?> create(
            key: String,
            modelClass: Class<T>
        ): T

        override fun <T : CvViewModel?> create(modelClass: Class<T>): T {
            throw UnsupportedOperationException("create(String, Class<?>) must be called on implementaions of KeyedFactory")
        }
    }

    constructor(owner: CvViewModelStoreOwner) : this(
        owner.getViewModelStore(), if (owner is CvHasDefaultViewModelProviderFactory) {
            (owner as CvHasDefaultViewModelProviderFactory).getDefaultViewModelProviderFactory()
        } else {
            CvNewInstanceFactory.getInstance()
        }
    )

    fun <T : CvViewModel> get(modelClass: Class<T>): T {
        val canonicalName = modelClass.canonicalName
            ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

        return get("$DEFAULT_KEY:$canonicalName", modelClass)
    }

    fun <T : CvViewModel> get(key: String, modelClass: Class<T>): T {
        var viewModel = mViewModelStore.get(key)

        if (modelClass.isInstance(viewModel)) {
            if (mFactory is OnRequeryFactory) {
                (mFactory as OnRequeryFactory).onRequery(viewModel)
            }
            return viewModel as T
        } else {
            if (viewModel != null) {

            }
        }
        viewModel = if (mFactory is KeyedFactory) {
            (mFactory as KeyedFactory).create(key, modelClass)
        } else {
            mFactory.create(modelClass)
        }

        mViewModelStore.put(key, viewModel)
        return viewModel
    }

    open class CvNewInstanceFactory : Factory {

        companion object {
            private var sInstance: CvNewInstanceFactory? = null

            fun getInstance(): CvNewInstanceFactory {
                if (sInstance == null) {
                    sInstance = CvNewInstanceFactory()
                }
                return sInstance!!
            }
        }

        override fun <T : CvViewModel?> create(modelClass: Class<T>): T {
            try {
                return modelClass.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        }
    }

    open class AndroidViewModelFactory(private val application: Application) :
        CvViewModelProvider.CvNewInstanceFactory() {

        companion object {
            private var sInstance: AndroidViewModelFactory? = null


            fun getInstance(application: Application): AndroidViewModelFactory {
                if (sInstance == null) {
                    sInstance = AndroidViewModelFactory(application)
                }
                return sInstance!!
            }

        }


        override fun <T : CvViewModel?> create(modelClass: Class<T>): T {
            if (CvAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
                try {
                    return modelClass.getConstructor(Application::class.java)
                        .newInstance(application)
                } catch (e: NoSuchMethodException) {
                    throw java.lang.RuntimeException("Cannot create an instance of $modelClass", e)
                } catch (e: IllegalAccessException) {
                    throw java.lang.RuntimeException("Cannot create an instance of $modelClass", e)
                } catch (e: InstantiationException) {
                    throw java.lang.RuntimeException("Cannot create an instance of $modelClass", e)
                } catch (e: InvocationTargetException) {
                    throw java.lang.RuntimeException("Cannot create an instance of $modelClass", e)
                }
            }
            return super.create(modelClass)
        }
    }
}