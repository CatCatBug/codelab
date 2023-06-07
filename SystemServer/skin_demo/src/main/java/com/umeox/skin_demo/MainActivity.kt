package com.umeox.skin_demo

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.umeox.skin_lib.SkinManager
import com.umeox.skin_lib.base.SkinActivity

class MainActivity : SkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("SuperSkinManager", "onCreate------------------------")


        findViewById<SwitchCompat>(R.id.sc_theme).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Utils.applyNight(this)
            } else {
                SkinManager.restoreDefaultTheme()
            }
        }

        findViewById<SwitchCompat>(R.id.sc_font).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SkinManager.applyTextFont(
                    mapOf(
                        Pair("montserrat_italic", "montserrat_black")
                    )
                )
            } else {
                SkinManager.applyTextFont(
                    mapOf(
                        Pair("montserrat_black", "montserrat_italic")
                    )
                )
            }
        }

        findViewById<TextView>(R.id.tv_native).setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }

        findViewById<TextView>(R.id.tv_mvvm).setOnClickListener {
//            startActivity(Intent(this, MvvmActivity::class.java))
            onActivityCreated(this)
        }

        findViewById<TextView>(R.id.tv_fragment).setOnClickListener {
            startActivity(Intent(this, FragmentActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<SwitchCompat>(R.id.sc_theme).isChecked = !SkinManager.isDefaultSkin()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(
            "SuperSkinManager",
            "onConfigurationChanged: ${(newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES}"
        )
    }

//    public static boolean isNightMode(Context context) {
//        try {
//            return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
//        } catch (Exception ex) {
//
//        }
//        return false;
//    }

    fun onActivityCreated(activity: Activity) {
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        activity.window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

}