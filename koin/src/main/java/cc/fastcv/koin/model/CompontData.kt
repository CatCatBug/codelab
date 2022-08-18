package cc.fastcv.koin.model

import android.util.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class CompontData : KoinComponent {
    val appD1 by inject<UserData>()//懒加载模式
    val appD2 = get<UserData>()//非懒加载模式
    fun printInfo() {
        Log.d("xcl_debug", "CompontData中appD1地址:" + appD1.hashCode() + "////appD2地址:" + appD2.hashCode())
    }
}