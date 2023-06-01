package com.umeox.logger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.initLogger(application)

        val scLogPrint = findViewById<SwitchCompat>(R.id.sc_log_print)
        scLogPrint.setOnCheckedChangeListener { _, isChecked ->
            Logger.logAble(isChecked)
        }


        val scLogSave = findViewById<SwitchCompat>(R.id.sc_log_save)
        scLogSave.setOnCheckedChangeListener { _, isChecked ->
            Logger.logSaveAble(isChecked)
        }

        findViewById<Button>(R.id.bt_main_log).setOnClickListener {
            Logger.i(TAG,"这是一句日志")
            Logger.w(TAG,"这是一句日志")
            Logger.d(TAG,"这是一句日志")
            Logger.e(TAG, "这是一句日志",NullPointerException("这是一个异常"))
        }

        findViewById<Button>(R.id.bt_t1_log).setOnClickListener {
            Thread {
                Logger.i(TAG, "这是一句日志")
                Logger.w(TAG, "这是一句日志")
                Logger.d(TAG, "这是一句日志")
                Logger.e(TAG, "这是一句日志",NullPointerException("这是一个异常"))
            }.start()
        }

        findViewById<Button>(R.id.bt_t2_log).setOnClickListener {
            Thread {
                Logger.i(TAG, "这是一句日志")
                Logger.w(TAG, "这是一句日志")
                Logger.d(TAG, "这是一句日志")
                Logger.e(TAG, "这是一句日志",NullPointerException("这是一个异常"))
            }.start()
        }

        findViewById<Button>(R.id.bt_t3_log).setOnClickListener {
            Thread {
                Logger.i(TAG, "这是一句日志")
                Logger.w(TAG, "这是一句日志")
                Logger.d(TAG, "这是一句日志")
                Logger.e(TAG, "这是一句日志",NullPointerException("这是一个异常"))
            }.start()
        }

        findViewById<Button>(R.id.bt_t4_log).setOnClickListener {
            Thread {
                Logger.i(TAG, "这是一句日志")
                Logger.w(TAG, "这是一句日志")
                Logger.d(TAG, "这是一句日志")
                Logger.e(TAG, "这是一句日志",NullPointerException("这是一个异常"))
            }.start()
        }

    }
}