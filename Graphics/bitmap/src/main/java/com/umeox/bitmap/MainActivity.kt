package com.umeox.bitmap

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {


    private var animator: Animator? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showAndHideAnimation()
    }

    private fun showAndHideAnimation() {
        val view = findViewById<View>(R.id.view)

        val btHide = findViewById<Button>(R.id.bt_hide)
        btHide.setOnClickListener {
            animator  = ViewAnimationUtils.createCircularReveal(
                view,
                view.width / 2,
                view.height / 2,
                view.width.toFloat(),
                0f
            )
            animator!!.interpolator = LinearInterpolator()
            animator!!.doOnEnd {
                view.visibility = View.INVISIBLE
            }
            animator!!.duration = 1000
            animator!!.start()
        }


        val btShow = findViewById<Button>(R.id.bt_show)
        btShow.setOnClickListener {
            view.visibility = View.VISIBLE
            animator  = ViewAnimationUtils.createCircularReveal(
                view, view.width / 2, view.height / 2, 0f, hypot(
                    view.width.toDouble(), view.height.toDouble()
                ).toFloat()
            )
            animator!!.duration = 1000
            animator!!.start()
        }
    }
}