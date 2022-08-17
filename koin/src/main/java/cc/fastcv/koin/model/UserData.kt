package cc.fastcv.koin.model

import android.util.Log

class UserData {
    var userName: String? = null
    var age: Int? = null
    fun info() {
        Log.d("xcl_debug", "用户名:$userName 年龄:$age")
    }
}