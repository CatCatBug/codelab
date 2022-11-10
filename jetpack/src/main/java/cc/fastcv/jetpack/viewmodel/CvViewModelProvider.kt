package cc.fastcv.jetpack.viewmodel

import androidx.lifecycle.ViewModel

class CvViewModelProvider(store: CvViewModelStore, factory: Factory) {

    companion object {
        private const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"
    }

    interface Factory {
        fun <T : ViewModel?> create(modelClass: Class<T>): T
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

        abstract fun <T : CvViewModel> create(modelClass: Class<T>): T
    }

    private var mFactory:Factory? = null
    private var mViewModelStore:CvViewModelStore? = null


    @JvmOverloads
    constructor(owner: CvViewModelStoreOwner) : this(
        owner.getViewModelStore(), if (owner is CvHasDefaultViewModelProviderFactory) {
            (owner as CvHasDefaultViewModelProviderFactory).getDefaultViewModelProviderFactory()
        } else {
            CvViewModelProvider.CvNewInstanceFactory.getInstance()
        }
    )

    fun <T : CvViewModel> get(modelClass: Class<T>) : T {
        val canonicalName = modelClass.canonicalName
            ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

        return get("$DEFAULT_KEY:$canonicalName", modelClass)
    }

    fun <T : CvViewModel> get(key:String, modelClass: Class<T>) : T {
        var viewModel = mViewModelStore!!.get(key)

        if (modelClass.isInstance(viewModel)) {
            if (mFactory is OnRequeryFactory) {
                (mFactory as OnRequeryFactory).onRequery(viewModel)
            }
            return viewModel as T
        } else {
            if (viewModel != null) {

            }
        }
        if (mFactory is KeyedFactory) {
            viewModel = (mFactory as KeyedFactory).create(key, modelClass)
        } else {
//            viewModel = mFactory.create(modelClass)
        }

        return modelClass.newInstance()


    }


    class CvNewInstanceFactory : Factory {

        companion object {
            private var sInstance: CvNewInstanceFactory? = null

            fun getInstance(): CvNewInstanceFactory {
                if (sInstance == null) {
                    sInstance = CvNewInstanceFactory()
                }
                return sInstance!!
            }
        }

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            try {
                return modelClass.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        }
    }

}