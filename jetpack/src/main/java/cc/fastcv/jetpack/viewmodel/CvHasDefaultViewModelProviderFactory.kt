package cc.fastcv.jetpack.viewmodel

import androidx.lifecycle.ViewModelProvider

interface CvHasDefaultViewModelProviderFactory {
    fun getDefaultViewModelProviderFactory(): CvViewModelProvider.Factory
}