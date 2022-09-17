package cc.fastcv.jetpack.livedata

import android.util.Log

object InfoListManager {

    private val deviceList = mutableListOf<Device>().apply {
        add(Device("A").apply { setDesc("A is init") })
        add(Device("B").apply { setDesc("B is init") })
        add(Device("C").apply { setDesc("C is init") })
        add(Device("D").apply { setDesc("D is init") })
        add(Device("E").apply { setDesc("E is init") })
        add(Device("F").apply { setDesc("F is init") })
    }

    private var mIndex = 0


    fun switch(index: Int) {
        Log.d("xcl_debug", "switch: index:$index")
        val oldDevice = deviceList[mIndex]
        oldDevice.reset()
        mIndex = index
        InfoProvider.replaceObservable(oldDevice.deviceInfo,deviceList[mIndex].deviceInfo)
    }

    fun setCurDeviceDes(desc:String) {
        deviceList[mIndex].setDesc(desc)
    }

}