package com.umeox.skin_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.umeox.skin_lib.base.SkinFragment
import com.umeox.skin_lib.config.SkinConfig
import com.umeox.skin_lib.loader.SkinManager

class DemoFragment : SkinFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!SkinConfig.isDefaultSkin()) {
            view.findViewById<SwitchCompat>(R.id.sc_theme).isChecked = true
        }

        view.findViewById<SwitchCompat>(R.id.sc_theme).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Utils.applyNight(requireContext())
            } else {
                SkinManager.restoreDefaultTheme()
            }
        }
    }

}