package cc.fastcv.jetpack.viewmodel

import android.app.Application

class CvAndroidViewModel(private val application: Application) : CvViewModel() {

    fun <T : Application?> getApplication(): T {
        return application as T
    }
}