package cc.fastcv.codelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cc.fastcv.codelab.date_switch_view.DateSwitchView
import cc.fastcv.codelab.date_switch_view.DayInfo

class MainActivity : AppCompatActivity(), DateSwitchView.DateSelectCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataSwitch = findViewById<DateSwitchView>(R.id.data_switch)
        dataSwitch.setDateSelectCallback(this)
        dataSwitch.setDateRange("2021-10-03","2022-09-22")
    }

    override fun onDateSelect(info: DayInfo) {
        Log.d("xcl_debug", "onDateSelect: $info")
    }
}