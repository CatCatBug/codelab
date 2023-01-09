package cc.fastcv.ext

import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Create by Eric
 * on 2023/1/7
 */
fun Fragment.toast(msg: String) {
    requireContext().toast(msg)
}

inline fun <reified T: Any> Fragment.startAct(
    action: () -> Unit = {}
) {
    startActivity(
        Intent(requireContext(), T::class.java).apply {
            action()
        }
    )
}

fun Fragment.pickPhoto(
    requestCode: Int,
    title: String = "选择获取图片的方式",
    action: (Intent, Int) -> Unit = {_,_->}
) {
    requireContext().pickPhoto(requestCode, title, action)
}
