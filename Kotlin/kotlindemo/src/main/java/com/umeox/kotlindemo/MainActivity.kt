package com.umeox.kotlindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.umeox.flow_event_bus.EventBusInitializer
import com.umeox.flow_event_bus.observeable.observeGlobalEvent
import com.umeox.flow_event_bus.observeable.observeScopeEvent
import com.umeox.kotlindemo.events.ActivityEvent
import com.umeox.kotlindemo.events.GlobalEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "xcl_debug"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBusInitializer.application = application

        findViewById<TextView>(R.id.tv).setOnClickListener {
            startActivity(Intent(this,MainBActivity::class.java))
        }


        observeGlobalEvent<GlobalEvent> {
            Log.d(TAG, "receive GlobalEvent1 $it ThreadName:${Thread.currentThread().name}")
        }

        observeGlobalEvent<GlobalEvent>(minActiveState = Lifecycle.State.CREATED) {
            Log.d(TAG, "receive GlobalEvent2 $it ThreadName:${Thread.currentThread().name}")
        }

        observeScopeEvent<ActivityEvent>(this) {
            Log.d(TAG, "receive ActivityEvent1 $it ThreadName:${Thread.currentThread().name}")
        }

        observeScopeEvent<ActivityEvent>(this, minActiveState = Lifecycle.State.CREATED) {
            Log.d(TAG, "receive ActivityEvent2 $it ThreadName:${Thread.currentThread().name}")
        }


    }
}