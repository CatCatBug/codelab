package cc.fastcv.jetpack.viewmodel

interface CvHasDefaultViewModelProviderFactory {
    fun getDefaultViewModelProviderFactory(): CvViewModelProvider.Factory
}