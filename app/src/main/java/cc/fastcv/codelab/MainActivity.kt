package cc.fastcv.codelab

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: ")


        findViewById<Button>(R.id.bt1).setOnClickListener {
//            startActivity(Intent(this,BActivity::class.java).apply {
//                component =
//                    ComponentName("cc.fastcv.codelab", "cc.fastcv.codelab.BActivity")
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })
            findViewById<CalendarView>(R.id.cv).visibility = View.GONE
        }


    }
}