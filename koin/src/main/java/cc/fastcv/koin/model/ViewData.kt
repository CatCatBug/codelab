package cc.fastcv.koin.model

import android.util.Log
import android.view.View

class ViewData(val view: View) {
    fun printId() {
        Log.d("xcl_debug", view.id.toString())
    }
}