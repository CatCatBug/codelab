package cc.fastcv.jetpack.livedata

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cc.fastcv.jetpack.R
import cc.fastcv.jetpack.databinding.ActivityLiveDataBinding
import kotlin.system.exitProcess

class LiveDataActivity : AppCompatActivity() {


    private val viewModel = ViewModel()

    private lateinit var mBinding: ActivityLiveDataBinding

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_live_data, null, false
        )

        // 绑定生命周期管理
        mBinding.lifecycleOwner = this

        // 设置布局
        setContentView(mBinding.root)

        mBinding.viewmodel = viewModel

        mBinding.tvSwitch.setOnClickListener {
            index++
            InfoListManager.switch(index % 6)
        }

        mBinding.tvModify.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                InfoListManager.setCurDeviceDes("我的值被修改了")
            }, 3000)
        }

        mBinding.tvExit.setOnClickListener {
            //Android 完全退出app程序（不保留历史打开app记录）
            finishAndRemoveTask();//该方法在API level 21之后添加。
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0);

        }


        InfoListManager.switch(0)

    }


}