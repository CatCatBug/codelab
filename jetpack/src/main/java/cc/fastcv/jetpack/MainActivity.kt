package cc.fastcv.jetpack

import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.lifecycleScope
import cc.fastcv.jetpack.component.BaseActivity
import cc.fastcv.jetpack.databinding.ActivityMainBinding
import cc.fastcv.jetpack.fragments.AFragment
import cc.fastcv.jetpack.fragments.BFragment
import cc.fastcv.jetpack.fragments.CFragment
import cc.fastcv.jetpack.fragments.FragmentUtils
import cc.fastcv.jetpack.viewmodel.CvViewModelProvider
import cc.fastcv.jetpack.viewmodel.CvViewModelStoreOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "xcl_debug"
    }

    private lateinit var binding: ActivityMainBinding

    lateinit var homeViewModel: MainViewModel

    private val googleHelper = GoogleHelper()
    private val googleHelper1 = GoogleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            Log.d(TAG, "FragmentOnAttach $fragment")
        }

        if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(homeViewModel.fragmentTags[0])?.let {
                Log.d(TAG, "恢复AFragment的引用 $it")
                homeViewModel.fragments[0] = it
            }

            supportFragmentManager.findFragmentByTag(homeViewModel.fragmentTags[1])?.let {
                Log.d(TAG, "恢复BFragment的引用 $it")
                homeViewModel.fragments[1] = it
            }

            supportFragmentManager.findFragmentByTag(homeViewModel.fragmentTags[2])?.let {
                Log.d(TAG, "恢复CFragment的引用 $it")
                homeViewModel.fragments[2] = it
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("xcl_adb", "执行命令")
                val exec = Runtime.getRuntime().exec("adb help")
                exec.waitFor()
                val mIn = exec.inputStream
                val read = BufferedReader(InputStreamReader(mIn))
                var result = read.readLine()
                Log.d("xcl_adb", "获取结果$result")
                while (result != null) {
                    Log.d("xcl_adb", result)
                    result = read.readLine()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        homeViewModel.mFragmentUtils.init(supportFragmentManager)

        homeViewModel.mFragmentUtils.showFragment(
            homeViewModel.fragments[0],
            homeViewModel.fragmentTags[0]
        )

        findViewById<Button>(R.id.bt1).setOnClickListener {
            homeViewModel.mFragmentUtils.showFragment(
                homeViewModel.fragments[0],
                homeViewModel.fragmentTags[0]
            )
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            homeViewModel.mFragmentUtils.showFragment(
                homeViewModel.fragments[1],
                homeViewModel.fragmentTags[1]
            )
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            homeViewModel.mFragmentUtils.showFragment(
                homeViewModel.fragments[2],
                homeViewModel.fragmentTags[2]
            )
        }

        googleHelper.init(this)
        googleHelper1.init(this)

        Log.d(TAG, "googleHelper $googleHelper")
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
    }

}