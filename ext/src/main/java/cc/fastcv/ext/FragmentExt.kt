package cc.fastcv.ext

import androidx.fragment.app.Fragment

/**
 * Create by Eric
 * on 2023/1/7
 */
fun Fragment.toast(msg: String) {
    requireContext().toast(msg)
}
