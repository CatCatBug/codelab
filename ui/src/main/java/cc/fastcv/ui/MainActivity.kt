package cc.fastcv.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import cc.fastcv.ui.rolling_counter.RollingCounterView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rolling = findViewById<RollingCounterView>(R.id.rolling)

        findViewById<Button>(R.id.bt_add).setOnClickListener {
            rolling.plus(100)
        }
    }
}