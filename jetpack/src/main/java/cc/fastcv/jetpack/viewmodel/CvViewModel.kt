package cc.fastcv.jetpack.viewmodel

import androidx.annotation.MainThread
import java.io.Closeable
import java.io.IOException
import java.lang.RuntimeException

abstract class CvViewModel {

    companion object {
        private fun closeWithRuntimeException(obj:Any?) {
            if (obj is Closeable) {
                try {
                    (obj as Closeable).close()
                } catch (e:IOException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    private val mBagOfTags = HashMap<String,Any>()

    @Volatile
    private var mCleared = false

    fun onCleared() {
    }

    @MainThread
    fun clear() {
        mCleared = true
        if (mBagOfTags != null) {
            synchronized(mBagOfTags) {
                for (value in mBagOfTags.values) {
                    closeWithRuntimeException(value)
                }
            }
        }
        onCleared()
    }

    fun <T> setTagIfAbsent(key:String, newValue:T) : T {
        var previous:Any?
        synchronized(mBagOfTags) {
            previous = mBagOfTags[key]
            if (previous == null) {
                mBagOfTags[key] = newValue!!
            }
        }
        val result = if (previous == null) {
            newValue
        } else {
            previous as T
        }
        if (mCleared) {
            closeWithRuntimeException(result)
        }
        return result
    }

    fun <T> getTag(key: String):T? {
        synchronized(mBagOfTags) {
            return mBagOfTags[key] as T?
        }
    }
}