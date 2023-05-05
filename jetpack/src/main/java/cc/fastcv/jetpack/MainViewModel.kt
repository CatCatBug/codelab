package cc.fastcv.jetpack

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import cc.fastcv.jetpack.fragments.AFragment
import cc.fastcv.jetpack.fragments.BFragment
import cc.fastcv.jetpack.fragments.CFragment
import cc.fastcv.jetpack.fragments.FragmentUtils

class MainViewModel : ViewModel() {

    //Fragment切换辅助类
    val mFragmentUtils: FragmentUtils by lazy {
        FragmentUtils(R.id.fl_container)
    }

    var fragments = mutableListOf<Fragment>(
        AFragment.instance,
        BFragment.instance,
        CFragment.instance
    )

    var fragmentTags = mutableListOf(
        "AFragment",
        "BFragment",
        "CFragment"
    )


}