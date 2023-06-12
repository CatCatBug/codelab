package com.umeox.alarm

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvLog = findViewById<TextView>(R.id.tv_log)
        tvLog.movementMethod = ScrollingMovementMethod.getInstance()

        findViewById<Button>(R.id.bt_do).setOnClickListener {
            AlarmTaskManager.startTask(this, getTime())
        }

        findViewById<Button>(R.id.bt_get_log).setOnClickListener {
            val file = File(externalCacheDir, "log.txt")
            if (file.exists()) {
                tvLog.text = file.readText()
            }
        }

//        val pendingIntent1 = PendingIntent.getBroadcast(
//            this,
//            0,
//            Intent(this, AlarmReceiver::class.java).apply {
//                putExtra("time", 1)
//                addCategory("1")
//            },
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val pendingIntent2 = PendingIntent.getBroadcast(
//            this,
//            0,
//            Intent(this, AlarmReceiver::class.java).apply {
//                putExtra("time", 2)
//                addCategory("2")
//            },
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        Log.d("xcl_debug", "pendingIntent1 = $pendingIntent1   pendingIntent2 = $pendingIntent2")
    }

    private fun getTime(): Long {
        return (findViewById<EditText>(R.id.et_time).text.toString().toLongOrNull()
            ?: (1)) * 60 * 60 * 1000
    }
}