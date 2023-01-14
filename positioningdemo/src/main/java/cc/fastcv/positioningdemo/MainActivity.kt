package cc.fastcv.positioningdemo

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import cc.fastcv.positioningdemo.baidu.BaiduMapActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val baidu: Button = findViewById(R.id.baidu)
        val gaode: Button = findViewById(R.id.gaode)
        val google: Button = findViewById(R.id.google)

        baidu.setOnClickListener {
            startActivity(Intent(this,BaiduMapActivity::class.java))
        }

        gaode.setOnClickListener {

        }

        google.setOnClickListener {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ), 2000
            )
        }
    }
}