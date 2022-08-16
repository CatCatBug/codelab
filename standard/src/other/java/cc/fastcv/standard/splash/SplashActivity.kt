package cc.fastcv.standard.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.standard.MainActivity
import cc.fastcv.standard.R


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Log.d("SplashActivity", "onCreate: metaChannel = ${getChannelFromAndroidManifest()}")

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }


    private fun getChannelFromAndroidManifest(): String {
        var metaChannel = ""
        try {
            val info = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            if (info.metaData == null) {
                return metaChannel
            }
            metaChannel = info.metaData.getString("channel").toString()
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return metaChannel
    }
}