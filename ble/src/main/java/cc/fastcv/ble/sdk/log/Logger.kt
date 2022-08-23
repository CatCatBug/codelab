package cc.fastcv.ble.sdk.log

import android.util.Log

object Logger {

    fun log(msg:String) {
        Log.i("sdk_log_info", "$msg   ---${Thread.currentThread().name}")
    }

}