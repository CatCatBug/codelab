package cc.fastcv.sqlite.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cc.fastcv.sqlite.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun intoStudentInfoOp(view: View) {
        StudentInfoOpActivity.intoActivity(this)
    }

    fun intoTeacherInfoOp(view: View) {
        StudentInfoOpActivity.intoActivity(this)
    }

    fun intoClassInfoOp(view: View) {
        StudentInfoOpActivity.intoActivity(this)
    }
}