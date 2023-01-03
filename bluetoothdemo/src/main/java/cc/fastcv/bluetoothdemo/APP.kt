package cc.fastcv.bluetoothdemo

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.widget.Toast

class APP : Application() {

    companion object {
        private val sHandler = Handler()
        private var sToast // 单例Toast,避免重复创建，显示时间过长
                : Toast? = null

        fun toast(txt: String?, duration: Int) {
            sToast!!.setText(txt)
            sToast!!.duration = duration
            sToast!!.show()
        }

        fun runUi(runnable: Runnable?) {
            sHandler.post(runnable!!)
        }
    }

    @SuppressLint("ShowToast")
    override fun onCreate() {
        super.onCreate()
        sToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
    }
}