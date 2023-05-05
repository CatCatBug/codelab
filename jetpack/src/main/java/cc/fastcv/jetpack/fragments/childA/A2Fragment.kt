package cc.fastcv.jetpack.fragments.childA

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.fastcv.jetpack.R
import cc.fastcv.jetpack.fragments.VisibilityFragment

class A2Fragment : VisibilityFragment() {

    companion object {
        val instance: A2Fragment
            get() = A2Fragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a2,container,false)
    }

    override fun onInvisible() {
        Log.d("xcl_visible", "onInvisible: A2")
    }

    override fun onVisible() {
        Log.d("xcl_visible", "onVisible: A2")
    }

}