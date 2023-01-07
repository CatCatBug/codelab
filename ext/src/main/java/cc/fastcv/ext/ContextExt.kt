package cc.fastcv.ext

import android.content.Context
import android.content.Intent

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
