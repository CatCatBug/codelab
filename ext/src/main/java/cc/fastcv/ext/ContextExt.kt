package cc.fastcv.ext

import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Create by Eric
 * on 2023/1/7
 */
fun Context.startAct(action: () -> Unit = {}) {
    startActivity(
        Intent().apply {
            action()
        }
    )
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}
