package cc.fastcv.ext

import android.util.Log

/**
 * Create by Eric
 * on 2023/1/7
 */
var TAG = "FastCVExt"

fun String.logI(tag: String = TAG) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, this)
    }
}

fun String.logW(tag: String = TAG) {
    if (BuildConfig.DEBUG) {
        Log.w(tag, this)
    }
}

fun String.logE(tag: String = TAG) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, this)
    }
}
