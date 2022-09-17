package cc.fastcv.jetpack.livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import cc.fastcv.jetpack.livedata.model.Info

object InfoProvider : Observer<Info> {

    private const val TAG = "InfoProvider"

    var info = MutableLiveData(Info())

    fun replaceObservable(oldLiveData: MutableLiveData<Info>, newLiveData: MutableLiveData<Info>) {
        Log.d(TAG, "replaceObservable: --- oldLiveData:${oldLiveData.value!!.name}      newLiveData:${newLiveData.value!!.name} ")
        oldLiveData.removeObserver(this)
        newLiveData.observeForever(this)
    }

    override fun onChanged(t: Info) {
        Log.d(TAG, "onChanged: $t")
        info.postValue(t)
    }

}