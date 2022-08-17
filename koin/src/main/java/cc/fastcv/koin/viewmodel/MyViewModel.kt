package cc.fastcv.koin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {
    var NumData :Int = 0

    override fun onCleared() {
        super.onCleared()
        Log.d("xcl_debug", "调用了销毁方法")
    }
}