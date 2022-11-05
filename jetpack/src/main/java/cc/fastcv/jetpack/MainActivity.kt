package cc.fastcv.jetpack

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewTreeLifecycleOwner
import cc.fastcv.jetpack.databinding.ActivityMainBinding
import cc.fastcv.jetpack.lifecycle.*
import cc.fastcv.jetpack.lifecycle.component.LifecycleActivity

class MainActivity : LifecycleActivity(), CvLifecycleEventObserver {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLifecycle().addObserver(object : CvLifecycleObserver {

            @CvOnLifecycleEvent(CvLifecycle.Event.ON_CREATE)
            fun create() {
                Log.d("xcl_debug1", "create: ")
            }


//            @CvOnLifecycleEvent(CvLifecycle.Event.ON_ANY)
//            fun testCall(owner: CvLifecycleOwner, event: CvLifecycle.Event) {
//                Log.d("xcl_debug1", "testCall: ")
//            }

        })


        binding.btBindLifecycle.setOnClickListener {
            Log.d("xcl_debug", "onCreate: 绑定生命周期")
            getLifecycle().addObserver(this)
        }
    }

    override fun onStart() {
        super.onStart()
//        getLifecycle().addObserver(object : CvFullLifecycleObserver {
//            override fun onCreate(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onCreate")
//            }
//
//            override fun onStart(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onStart")
//            }
//
//            override fun onResume(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onResume")
//            }
//
//            override fun onPause(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onPause")
//            }
//
//            override fun onStop(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onStop")
//            }
//
//            override fun onDestroy(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onDestroy")
//            }
//
//        })
    }

    override fun onPause() {
        super.onPause()
//        getLifecycle().addObserver(object : CvFullLifecycleObserver {
//            override fun onCreate(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onCreate")
//            }
//
//            override fun onStart(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onStart")
//            }
//
//            override fun onResume(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onResume")
//            }
//
//            override fun onPause(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onPause")
//            }
//
//            override fun onStop(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onStop")
//            }
//
//            override fun onDestroy(owner: CvLifecycleOwner) {
//                Log.d("xcl_debug", "Observer2: onDestroy")
//            }
//
//        })
    }

    override fun onStateChanged(source: CvLifecycleOwner, event: CvLifecycle.Event) {
        Log.d("xcl_debug", "onStateChanged: $event")
    }


}