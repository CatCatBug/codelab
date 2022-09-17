package cc.fastcv.jetpack.livedata

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cc.fastcv.jetpack.R
import cc.fastcv.jetpack.databinding.ActivityLiveDataBinding

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


        InfoListManager.switch(0)

    }


}