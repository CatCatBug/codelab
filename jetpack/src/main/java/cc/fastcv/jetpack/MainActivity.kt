package cc.fastcv.jetpack

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cc.fastcv.jetpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    val homeViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val custom = lastCustomNonConfigurationInstance

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