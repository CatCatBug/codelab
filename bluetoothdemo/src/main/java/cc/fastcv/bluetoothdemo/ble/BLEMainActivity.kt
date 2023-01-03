package cc.fastcv.bluetoothdemo.ble

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.ble.client.BLEClientActivity
import cc.fastcv.bluetoothdemo.ble.server.BLEServerActivity

class BLEMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_main)

        val btClient = findViewById<Button>(R.id.bt_client)
        btClient.setOnClickListener {
            startActivity(Intent(this, BLEClientActivity::class.java))
        }

        val btServer = findViewById<Button>(R.id.bt_server)
        btServer.setOnClickListener {
            startActivity(Intent(this, BLEServerActivity::class.java))
        }
    }

}