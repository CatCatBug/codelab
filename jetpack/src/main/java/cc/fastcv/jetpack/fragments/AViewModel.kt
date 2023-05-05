package cc.fastcv.jetpack.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import cc.fastcv.jetpack.R
import cc.fastcv.jetpack.fragments.childA.A1Fragment
import cc.fastcv.jetpack.fragments.childA.A2Fragment
import cc.fastcv.jetpack.fragments.childA.A3Fragment

class AViewModel : ViewModel() {

    //Fragment切换辅助类
    val mFragmentUtils: FragmentUtils by lazy {
        FragmentUtils(R.id.fl_container)
    }

    var fragments = mutableListOf<Fragment>(
        A1Fragment.instance,
        A2Fragment.instance,
        A3Fragment.instance
    )

    var fragmentTags = mutableListOf(
        "A1Fragment",
        "A2Fragment",
        "A3Fragment"
    )

}