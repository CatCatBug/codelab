package com.umeox.workmanager

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvLog = findViewById<TextView>(R.id.tv_log)
        tvLog.movementMethod = ScrollingMovementMethod.getInstance()

        findViewById<Button>(R.id.bt_do).setOnClickListener {
            WorkManagerTaskManager.startTask(this,getTime())
        }

        findViewById<Button>(R.id.bt_get_log).setOnClickListener {
            val file = File(externalCacheDir, "log.txt")
            if (file.exists()) {
                tvLog.text = file.readText()
            }
            val workInfosByTag =
                WorkManager.getInstance(this).getWorkInfosByTag(WorkManagerTaskManager.tag)
            val get = workInfosByTag.get()
            for (workInfo in get) {
                Log.d("xcl_debug", "$workInfo")
            }
        }
    }

    private fun getTime() : Long {
        return findViewById<EditText>(R.id.et_time).text.toString().toLongOrNull() ?: (60 * 1000)
    }
}