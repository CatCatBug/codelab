package cc.fastcv.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Thread.dumpStack()
        for (fragment in childFragmentManager.fragments) {
            fragment.onHiddenChanged(hidden)
        }
    }

    override fun onResume() {
        super.onResume()
        onHiddenChanged(false)
    }

    override fun onPause() {
        super.onPause()
        onHiddenChanged(true)
    }

}