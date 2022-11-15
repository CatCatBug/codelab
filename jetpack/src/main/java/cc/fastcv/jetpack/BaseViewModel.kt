package cc.fastcv.jetpack

import cc.fastcv.jetpack.viewmodel.CvViewModel
import cc.fastcv.jetpack.viewmodel.viewModelScope
import kotlinx.coroutines.launch

class BaseViewModel : CvViewModel() {

    fun te() {
        viewModelScope.launch {

        }
    }

}