package cc.fastcv.codelab

import android.content.*
import android.os.*
import android.util.Log
import android.widget.Button
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

        }

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        registerReceiver(DateTime(), filter)
    }

    class DateTime : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "收到广播")
            if (intent == null) return
            val action = intent.action
            if (action == null || action.isEmpty()) return
            if (action == Intent.ACTION_TIMEZONE_CHANGED) {
                //系统每1分钟发送一次广播
                Log.d(TAG, "设置了系统时区")
            } else if (action == Intent.ACTION_DATE_CHANGED) {
                //系统手动更改时间发送广播
                Log.d(TAG, "设置了系统时间")
            } else if (action == Intent.ACTION_TIME_TICK) {
                //系统手动更改时间发送广播
                Log.d(TAG, "每分钟变化")
            } else if (action == Intent.ACTION_TIME_CHANGED) {
                //系统手动更改时间发送广播
                Log.d(TAG, "时间变化")
            }
        }
    }
}