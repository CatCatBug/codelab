package com.umeox.skin_lib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.umeox.skin_lib.entity.DynamicAttr
import com.umeox.skin_lib.listener.IDynamicNewView

class SkinFragment : Fragment() {

    private var mIDynamicNewView: IDynamicNewView? = null

    override fun onAttach(context: Context) {
        super.onAttach(requireContext())
        mIDynamicNewView = try {
            context as IDynamicNewView?
        } catch (e: ClassCastException) {
            null
        }
    }

    fun dynamicAddView(view: View?, pDAttrs: List<DynamicAttr>) {
        if (mIDynamicNewView == null) {
            throw RuntimeException("IDynamicNewView should be implements !")
        } else {
            mIDynamicNewView!!.dynamicAddView(view, pDAttrs)
        }
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        return requireActivity().layoutInflater
    }
}