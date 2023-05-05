package cc.fastcv.jetpack

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class GoogleHelper {

    companion object {
        private const val TAG = "xcl_debug"
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var launcherCallback: ActivityResultCallback<ActivityResult> =
        ActivityResultCallback<ActivityResult> { result ->
            Log.d(TAG, "哈哈哈 ")
        }

    /**
     * 初始化谷歌登录助手
     */
    fun init(activity: MainActivity) {
        resultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            launcherCallback
        )
    }

}