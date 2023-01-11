package cc.fastcv.ext

import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Create by Eric
 * on 2023/1/7
 */
inline fun <reified T: Any> Context.start(
    action: Intent.() -> Unit = {}
) {
    startActivity(
        Intent(this, T::class.java).apply {
            action()
        }
    )
}

fun Context.pickPhoto(
    requestCode: Int,
    title: String = "选择获取图片的方式",
    action: (Intent, Int) -> Unit = {_,_->}
) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    action(Intent.createChooser(intent, title), requestCode)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}
