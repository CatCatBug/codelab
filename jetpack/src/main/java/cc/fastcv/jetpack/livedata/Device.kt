package cc.fastcv.jetpack.livedata

import androidx.lifecycle.MutableLiveData
import cc.fastcv.jetpack.livedata.model.Info

class Device(nameStr:String) {

    fun setDesc(desc: String) {
        val info = deviceInfo.value!!
        info.desc = desc
        deviceInfo.postValue(info)
    }

    fun reset() {
        val info = deviceInfo.value!!
        info.desc = "${info.name} is init"
        deviceInfo.postValue(info)
    }

    val deviceInfo = MutableLiveData(Info().apply { name = nameStr })

}