package cc.fastcv.koin.model

import android.app.Application
import android.util.Log

class NormalData {
    var numData: Int = 0
    var userName: String = ""
    var mApp: Application? = null
    var appData: AppData? = null

    constructor(userName: String, numData: Int) {
        this.userName = userName
        this.numData = numData
    }

    constructor(appData: AppData) {
        this.appData = appData
    }

    constructor(mApp: Application) {
        this.mApp = mApp
    }

    fun printInfo(str: String) {//打印里面的信息
        Log.d(
            "xcl_debug",
            "$str 的信息 numData:$numData userName:$userName application是否为空:${(mApp == null)} appData是否为空:${(appData == null)}"
        )
    }
}