package cc.fastcv.jetpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BaseViewModel : ViewModel() {

    fun te() {
        viewModelScope.launch {

        }
    }

}