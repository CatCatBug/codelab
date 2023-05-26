package com.umeox.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.umeox.flow_event_bus.observeable.observeGlobalEvent
import com.umeox.flow_event_bus.observeable.observeScopeEvent
import com.umeox.flow_event_bus.post.postEvent
import com.umeox.flow_event_bus.post.postGlobalEvent
import com.umeox.kotlindemo.events.ActivityEvent
import com.umeox.kotlindemo.events.GlobalEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainBActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "xcl_debug1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainb)


        val tv:TextView = findViewById(R.id.tv)

        findViewById<Button>(R.id.btGlobal).setOnClickListener {
            postGlobalEvent(GlobalEvent().apply {
                arg1 = "1"
                arg2 = "2"
            })
        }

        findViewById<Button>(R.id.btActivity).setOnClickListener {
            postEvent(ActivityEvent().apply {
                arg1 = "3"
                arg2 = "4"
            })
        }

        findViewById<Button>(R.id.btActivityDelay).setOnClickListener {
            postEvent(ActivityEvent().apply {
                arg1 = "3"
                arg2 = "4"
            },5000L)
        }


        observeGlobalEvent<GlobalEvent> {
            Log.d(TAG, "receive GlobalEvent1 $it ThreadName:${Thread.currentThread().name}")
            tv.text = "bbb"
        }

        observeGlobalEvent<GlobalEvent>(minActiveState = Lifecycle.State.CREATED) {
            Log.d(TAG, "receive GlobalEvent2 $it ThreadName:${Thread.currentThread().name}")
            tv.text = "bbb"
        }

        observeScopeEvent<ActivityEvent>(this) {
            Log.d(TAG, "receive ActivityEvent1 $it ThreadName:${Thread.currentThread().name}")
            tv.text = "bbb"
        }

        observeScopeEvent<ActivityEvent>(this, minActiveState = Lifecycle.State.CREATED) {
            Log.d(TAG, "receive ActivityEvent2 $it ThreadName:${Thread.currentThread().name}")
            tv.text = "bbb"
        }

        observeGlobalEvent<GlobalEvent>(isSticky = true) {
            Log.d(TAG, "receive GlobalEvent1 $it ThreadName:${Thread.currentThread().name}")
            tv.text = "bbb"
        }


    }
}