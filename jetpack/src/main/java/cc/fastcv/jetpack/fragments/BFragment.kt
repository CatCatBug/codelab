package cc.fastcv.jetpack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.fastcv.jetpack.R

class BFragment : VisibilityFragment() {

    companion object {
        val instance: BFragment
            get() = BFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b,container,false)
    }

    override fun onInvisible() {
        Log.d("xcl_visible", "onInvisible: B")
    }

    override fun onVisible() {
        Log.d("xcl_visible", "onVisible: B")
    }

}