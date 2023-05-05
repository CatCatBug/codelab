package cc.fastcv.jetpack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import cc.fastcv.jetpack.MainActivity
import cc.fastcv.jetpack.MainViewModel
import cc.fastcv.jetpack.R

class AFragment : VisibilityFragment() {

    companion object {
        private const val TAG = "xcl_debug"
        val instance: AFragment
            get() = AFragment()
    }

    lateinit var homeViewModel: AViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_a,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(AViewModel::class.java)

        if (savedInstanceState!= null) {
            childFragmentManager.findFragmentByTag(homeViewModel.fragmentTags[0])?.let {
                Log.d(TAG, "恢复A1Fragment的引用 $it")
                homeViewModel.fragments[0] = it
            }

            childFragmentManager.findFragmentByTag(homeViewModel.fragmentTags[1])?.let {
                Log.d(TAG, "恢复A2Fragment的引用 $it")
                homeViewModel.fragments[1] = it
            }

            childFragmentManager.findFragmentByTag(homeViewModel.fragmentTags[2])?.let {
                Log.d(TAG, "恢复A3Fragment的引用 $it")
                homeViewModel.fragments[2] = it
            }
        }


        homeViewModel.mFragmentUtils.init(childFragmentManager)

        homeViewModel.mFragmentUtils.showFragment(homeViewModel.fragments[0],homeViewModel.fragmentTags[0])

        view.findViewById<Button>(R.id.bt1).setOnClickListener {
            homeViewModel.mFragmentUtils.showFragment(homeViewModel.fragments[0],homeViewModel.fragmentTags[0])
        }

        view.findViewById<Button>(R.id.bt2).setOnClickListener {
            homeViewModel.mFragmentUtils.showFragment(homeViewModel.fragments[1],homeViewModel.fragmentTags[1])
        }

        view.findViewById<Button>(R.id.bt3).setOnClickListener {
            homeViewModel.mFragmentUtils.showFragment(homeViewModel.fragments[2],homeViewModel.fragmentTags[2])
        }
    }

    override fun onInvisible() {
        Log.d("xcl_visible", "onInvisible: A")
    }

    override fun onVisible() {
        Log.d("xcl_visible", "onVisible: A")
    }
}