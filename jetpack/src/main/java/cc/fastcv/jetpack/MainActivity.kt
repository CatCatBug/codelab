package cc.fastcv.jetpack

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import cc.fastcv.jetpack.component.BaseActivity
import cc.fastcv.jetpack.databinding.ActivityMainBinding
import cc.fastcv.jetpack.viewmodel.CvViewModelProvider
import cc.fastcv.jetpack.viewmodel.CvViewModelStoreOwner

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    lateinit var homeViewModel:BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        homeViewModel = CvViewModelProvider(this).get(BaseViewModel::class.java)

        binding.btBindLifecycle.setOnClickListener {
            Log.d("xcl_debug", "onCreate: 绑定生命周期")
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        Log.d(TAG, "onRetainCustomNonConfigurationInstance: ")
        Thread.dumpStack();
        return null
    }

}