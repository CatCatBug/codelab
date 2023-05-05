package cc.fastcv.jetpack.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * R.id.fl_container
 */
class FragmentUtils(private val containerId: Int) {

    companion object {
        private const val TAG = "xcl_debug"

        fun showFragmentInfo(tag: String,fragment: Fragment) {
            Log.d(
                tag, "打印fragment信息: \n" +
                        "isAdded: ${fragment.isAdded} \n" +
                        "isDetached: ${fragment.isDetached} \n" +
                        "isHidden: ${fragment.isHidden} \n" +
                        "isRemoving: ${fragment.isRemoving} \n" +
                        "isInLayout: ${fragment.isInLayout} \n" +
                        "isResumed: ${fragment.isResumed} \n" +
                        "isStateSaved: ${fragment.isStateSaved} \n" +
                        "isVisible: ${fragment.isVisible} \n" +
                        ""
            )
        }
    }

    //当前选中的Fragment
    private var mCurrentFragmentTag: String? = null
    private lateinit var fragmentManager: FragmentManager

    fun init(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
        Log.d(TAG, "init: fragmentManager = $fragmentManager")
    }

    fun showFragment(fragment: Fragment, tag: String) {
        //如果当前点击的fragment相同，则不处理点击事件
        if (tag == mCurrentFragmentTag) {
            Log.i(TAG, "tag == mCurrentFragmentTag , return !")
            return
        }

        if (!fragment.isAdded) {
            Log.i(TAG, "fragment 未被添加!")
            fragmentManager.beginTransaction().add(containerId, fragment, tag)
                .commitAllowingStateLoss()
        }

        val beginTransaction = fragmentManager.beginTransaction()
        //如果上一个fragment是空，则不需要隐藏上一个fragment
        if (mCurrentFragmentTag != null) {
            val findFragmentByTag = fragmentManager.findFragmentByTag(mCurrentFragmentTag)
            if (findFragmentByTag != null) {
                Log.i(TAG, "上一个不为空，隐藏$findFragmentByTag")
                beginTransaction.hide(findFragmentByTag)
            }
        }

        Log.i(TAG, "开始展示$fragment")
        beginTransaction.show(fragment).commitAllowingStateLoss()
        mCurrentFragmentTag = tag
    }


}